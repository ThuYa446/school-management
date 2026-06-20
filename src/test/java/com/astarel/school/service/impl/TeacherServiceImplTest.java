package com.astarel.school.service.impl;

import static com.astarel.school.TestDataFactory.studentEntity;
import static com.astarel.school.TestDataFactory.subjectDto;
import static com.astarel.school.TestDataFactory.subjectEntity;
import static com.astarel.school.TestDataFactory.teacherDto;
import static com.astarel.school.TestDataFactory.teacherEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.astarel.school.exception.ApiErrorResponse;
import com.astarel.school.model.dto.TeacherDto;
import com.astarel.school.model.entity.Subject;
import com.astarel.school.model.entity.Teacher;
import com.astarel.school.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
class TeacherServiceImplTest {

	@Mock
	private TeacherRepository teacherRepository;

	private TeacherServiceImpl service;

	@BeforeEach
	void setUp() {
		service = new TeacherServiceImpl();
		service.teacherRepository = teacherRepository;
	}

	@Test
	void getTeacherByIdMapsNestedSubjectsAndStudents() {
		Teacher teacher = teacherEntity(1L);
		Subject subject = subjectEntity(10L);
		subject.setStudent(List.of(studentEntity(20L)));
		teacher.setSubject(List.of(subject));
		when(teacherRepository.findTeacherById(1L)).thenReturn(Optional.of(teacher));

		TeacherDto result = service.getTeacherById(1L);

		assertThat(result.getSubjects()).hasSize(1);
		assertThat(result.getSubjects().get(0).getStudentDto()).hasSize(1);
	}

	@Test
	void saveTeacherPersistsWhenEmailIsUniqueAndLinksSubjects() throws Exception {
		TeacherDto dto = teacherDto(1L);
		dto.setSubjects(List.of(subjectDto(10L)));
		when(teacherRepository.findTeacherByEmail(dto.getEmail())).thenReturn(Optional.empty());
		when(teacherRepository.save(org.mockito.ArgumentMatchers.any(Teacher.class))).thenAnswer(invocation -> invocation.getArgument(0));

		TeacherDto result = service.saveTeacher(dto);

		assertThat(result.getSubjects()).hasSize(1);
		assertThat(result.getSubjects().get(0).getTitle()).isEqualTo("Subject 10");
	}

	@Test
	void saveTeacherRejectsDuplicateEmail() {
		TeacherDto dto = teacherDto(1L);
		when(teacherRepository.findTeacherByEmail(dto.getEmail())).thenReturn(Optional.of(teacherEntity(99L)));

		assertThatThrownBy(() -> service.saveTeacher(dto))
				.isInstanceOf(ApiErrorResponse.class)
				.extracting("errorCode").isEqualTo("1001");
	}

	@Test
	void updateTeacherPersistsWhenEmailBelongsToSameTeacher() throws Exception {
		TeacherDto dto = teacherDto(1L);
		dto.setSubjects(List.of(subjectDto(10L)));
		Teacher existing = teacherEntity(1L);
		when(teacherRepository.findTeacherById(1L)).thenReturn(Optional.of(existing));
		when(teacherRepository.findTeacherByEmail(dto.getEmail())).thenReturn(Optional.of(existing));
		when(teacherRepository.save(org.mockito.ArgumentMatchers.any(Teacher.class))).thenAnswer(invocation -> invocation.getArgument(0));

		TeacherDto result = service.updateTeacher(dto);

		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getSubjects()).hasSize(1);
	}

	@Test
	void updateTeacherRejectsInvalidStates() {
		TeacherDto dto = teacherDto(1L);
		Teacher otherTeacher = teacherEntity(2L);
		otherTeacher.setEmail(dto.getEmail());
		when(teacherRepository.findTeacherById(1L)).thenReturn(Optional.of(teacherEntity(1L)));
		when(teacherRepository.findTeacherByEmail(dto.getEmail())).thenReturn(Optional.of(otherTeacher));

		assertThatThrownBy(() -> service.updateTeacher(dto))
				.isInstanceOf(ApiErrorResponse.class)
				.extracting("errorCode").isEqualTo("1003");

		when(teacherRepository.findTeacherByEmail(dto.getEmail())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.updateTeacher(dto))
				.isInstanceOf(ApiErrorResponse.class)
				.extracting("errorCode").isEqualTo("1004");

		when(teacherRepository.findTeacherById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.updateTeacher(dto))
				.isInstanceOf(ApiErrorResponse.class)
				.extracting("errorCode").isEqualTo("1002");
	}
}
