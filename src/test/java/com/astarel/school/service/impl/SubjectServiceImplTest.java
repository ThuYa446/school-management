package com.astarel.school.service.impl;

import static com.astarel.school.TestDataFactory.classRoomEntity;
import static com.astarel.school.TestDataFactory.studentDto;
import static com.astarel.school.TestDataFactory.subjectDto;
import static com.astarel.school.TestDataFactory.subjectEntity;
import static com.astarel.school.TestDataFactory.teacherEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.astarel.school.exception.ApiErrorResponse;
import com.astarel.school.model.dto.SubjectDto;
import com.astarel.school.model.entity.Subject;
import com.astarel.school.repository.ClassRoomRepository;
import com.astarel.school.repository.SubjectRepository;
import com.astarel.school.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
class SubjectServiceImplTest {

	@Mock
	private SubjectRepository subjectRepository;

	@Mock
	private ClassRoomRepository classRoomRepository;

	@Mock
	private TeacherRepository teacherRepository;

	private SubjectServiceImpl service;

	@BeforeEach
	void setUp() {
		service = new SubjectServiceImpl();
		service.subjectRepository = subjectRepository;
		service.classRoomRepository = classRoomRepository;
		service.teacherRepository = teacherRepository;
	}

	@Test
	void getAllSubjectsAndNotTaughtSubjectsUseRepositoryData() {
		when(subjectRepository.findAll()).thenReturn(List.of(subjectEntity(1L)));
		when(subjectRepository.findByTeacherIsNull()).thenReturn(List.of(subjectEntity(2L)));

		assertThat(service.getAllSubjects()).hasSize(1);
		assertThat(service.getSubjectNotTeachByTeacher()).hasSize(1);
	}

	@Test
	void saveSubjectPersistsUniqueTitleAndBackfillsRelations() throws Exception {
		SubjectDto dto = subjectDto(1L);
		dto.setStudentDto(List.of(studentDto(11L)));
		when(classRoomRepository.getClassRoomByStudentId(11L)).thenReturn(Optional.of(classRoomEntity(50L)));
		when(teacherRepository.getTeacherBySubjectId(1L)).thenReturn(Optional.of(teacherEntity(60L)));
		when(subjectRepository.findSubjectByTitle(dto.getTitle())).thenReturn(Optional.empty());
		when(subjectRepository.save(org.mockito.ArgumentMatchers.any(Subject.class))).thenAnswer(invocation -> invocation.getArgument(0));

		SubjectDto result = service.saveSubject(dto);

		assertThat(result.getTitle()).isEqualTo("Subject 1");
		assertThat(result.getStudentDto()).hasSize(1);
	}

	@Test
	void saveSubjectRejectsDuplicateTitle() {
		SubjectDto dto = subjectDto(1L);
		when(subjectRepository.findSubjectByTitle(dto.getTitle())).thenReturn(Optional.of(subjectEntity(90L)));

		assertThatThrownBy(() -> service.saveSubject(dto))
				.isInstanceOf(ApiErrorResponse.class)
				.extracting("errorCode").isEqualTo("1001");
	}

	@Test
	void updateSubjectPersistsWhenTitleBelongsToSameSubject() throws Exception {
		SubjectDto dto = subjectDto(1L);
		dto.setStudentDto(List.of(studentDto(11L)));
		Subject existing = subjectEntity(1L);
		when(classRoomRepository.getClassRoomByStudentId(11L)).thenReturn(Optional.of(classRoomEntity(50L)));
		when(teacherRepository.getTeacherBySubjectId(1L)).thenReturn(Optional.of(teacherEntity(60L)));
		when(subjectRepository.findSubjectById(1L)).thenReturn(Optional.of(existing));
		when(subjectRepository.findSubjectByTitle(dto.getTitle())).thenReturn(Optional.of(existing));
		when(subjectRepository.save(org.mockito.ArgumentMatchers.any(Subject.class))).thenAnswer(invocation -> invocation.getArgument(0));

		SubjectDto result = service.updateSubject(dto);

		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getStudentDto()).hasSize(1);
	}

	@Test
	void updateSubjectRejectsDuplicateAndMissingIds() {
		SubjectDto dto = subjectDto(1L);
		when(subjectRepository.findSubjectById(1L)).thenReturn(Optional.of(subjectEntity(1L)));
		Subject otherSubject = subjectEntity(2L);
		otherSubject.setTitle(dto.getTitle());
		when(subjectRepository.findSubjectByTitle(dto.getTitle())).thenReturn(Optional.of(otherSubject));

		assertThatThrownBy(() -> service.updateSubject(dto))
				.isInstanceOf(ApiErrorResponse.class)
				.extracting("errorCode").isEqualTo("1003");

		when(subjectRepository.findSubjectById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.updateSubject(dto))
				.isInstanceOf(ApiErrorResponse.class)
				.extracting("errorCode").isEqualTo("1002");
	}

	@Test
	void deleteSubjectDelegatesToRepository() {
		service.deleteSubjectById(12L);

		verify(subjectRepository).deleteById(12L);
	}
}
