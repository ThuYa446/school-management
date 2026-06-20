package com.astarel.school.service.impl;

import static com.astarel.school.TestDataFactory.studentDto;
import static com.astarel.school.TestDataFactory.studentEntity;
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
import com.astarel.school.model.dto.StudentDto;
import com.astarel.school.model.entity.Student;
import com.astarel.school.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

	@Mock
	private StudentRepository studentRepository;

	private StudentServiceImpl service;

	@BeforeEach
	void setUp() {
		service = new StudentServiceImpl();
		service.studentRepository = studentRepository;
	}

	@Test
	void getAllStudentsMapsRepositoryEntitiesToDtos() {
		when(studentRepository.findAll()).thenReturn(List.of(studentEntity(1L), studentEntity(2L)));

		List<StudentDto> result = service.getAllStudents();

		assertThat(result).extracting(StudentDto::getEmail)
				.containsExactly("student1@school.com", "student2@school.com");
	}

	@Test
	void getStudentFiltersUseRepositoryResults() {
		when(studentRepository.findBySubjectIsNull()).thenReturn(List.of(studentEntity(1L)));
		when(studentRepository.findByClassRoomIsNull()).thenReturn(List.of(studentEntity(2L)));

		assertThat(service.getStudentNotEnrolledSubject()).hasSize(1);
		assertThat(service.getStudentNotJoinedClass()).hasSize(1);
	}

	@Test
	void saveStudentPersistsWhenEmailIsUnique() throws Exception {
		StudentDto dto = studentDto(1L);
		Student saved = studentEntity(1L);
		when(studentRepository.findStudentByEmail(dto.getEmail())).thenReturn(Optional.empty());
		when(studentRepository.save(org.mockito.ArgumentMatchers.any(Student.class))).thenReturn(saved);

		StudentDto result = service.saveStudent(dto);

		assertThat(result.getEmail()).isEqualTo(dto.getEmail());
	}

	@Test
	void saveStudentRejectsDuplicateEmail() {
		StudentDto dto = studentDto(1L);
		when(studentRepository.findStudentByEmail(dto.getEmail())).thenReturn(Optional.of(studentEntity(99L)));

		assertThatThrownBy(() -> service.saveStudent(dto))
				.isInstanceOf(ApiErrorResponse.class)
				.extracting("errorCode").isEqualTo("1001");
	}

	@Test
	void updateStudentPersistsWhenExistingEmailBelongsToSameStudent() throws Exception {
		StudentDto dto = studentDto(1L);
		Student existing = studentEntity(1L);
		when(studentRepository.findStudentById(1L)).thenReturn(Optional.of(existing));
		when(studentRepository.findStudentByEmail(dto.getEmail())).thenReturn(Optional.of(existing));
		when(studentRepository.save(org.mockito.ArgumentMatchers.any(Student.class))).thenReturn(existing);

		StudentDto result = service.updateStudent(dto);

		assertThat(result.getId()).isEqualTo(1L);
	}

	@Test
	void updateStudentRejectsEmailUsedByAnotherStudent() {
		StudentDto dto = studentDto(1L);
		Student other = studentEntity(2L);
		other.setEmail(dto.getEmail());
		when(studentRepository.findStudentById(1L)).thenReturn(Optional.of(studentEntity(1L)));
		when(studentRepository.findStudentByEmail(dto.getEmail())).thenReturn(Optional.of(other));

		assertThatThrownBy(() -> service.updateStudent(dto))
				.isInstanceOf(ApiErrorResponse.class)
				.extracting("errorCode").isEqualTo("1003");
	}

	@Test
	void updateStudentRejectsUnknownEmailAndMissingId() {
		StudentDto dto = studentDto(1L);
		when(studentRepository.findStudentById(1L)).thenReturn(Optional.of(studentEntity(1L)));
		when(studentRepository.findStudentByEmail(dto.getEmail())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.updateStudent(dto))
				.isInstanceOf(ApiErrorResponse.class)
				.extracting("errorCode").isEqualTo("1004");

		when(studentRepository.findStudentById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.updateStudent(dto))
				.isInstanceOf(ApiErrorResponse.class)
				.extracting("errorCode").isEqualTo("1002");
	}

	@Test
	void deleteStudentByIdDelegatesToRepository() {
		service.deleteStudentById(5L);

		verify(studentRepository).deleteById(5L);
	}
}
