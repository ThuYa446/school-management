package com.astarel.school.service.impl;

import static com.astarel.school.TestDataFactory.classRoomDto;
import static com.astarel.school.TestDataFactory.classRoomEntity;
import static com.astarel.school.TestDataFactory.studentDto;
import static com.astarel.school.TestDataFactory.subjectEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.astarel.school.exception.ApiErrorResponse;
import com.astarel.school.model.dto.ClassRoomDto;
import com.astarel.school.model.entity.ClassRoom;
import com.astarel.school.repository.ClassRoomRepository;
import com.astarel.school.repository.SubjectRepository;

@ExtendWith(MockitoExtension.class)
class ClassRoomServiceImplTest {

	@Mock
	private ClassRoomRepository classRoomRepository;

	@Mock
	private SubjectRepository subjectRepository;

	private ClassRoomServiceImpl service;

	@BeforeEach
	void setUp() {
		service = new ClassRoomServiceImpl();
		service.classRoomRepository = classRoomRepository;
		service.subjectRepository = subjectRepository;
	}

	@Test
	void getAllClassRoomsMapsRepositoryEntities() {
		when(classRoomRepository.findAll()).thenReturn(List.of(classRoomEntity(1L)));

		assertThat(service.getAllClassRoom()).hasSize(1);
	}

	@Test
	void saveClassRoomPersistsWhenNameIsUniqueAndCapacityIsValid() throws Exception {
		ClassRoomDto dto = classRoomDto(1L);
		dto.setStudentDto(List.of(studentDto(11L)));
		when(subjectRepository.getSubjectByStudentId(11L)).thenReturn(Optional.of(subjectEntity(21L)));
		when(classRoomRepository.findClassRoomByClassName(dto.getClassName())).thenReturn(Optional.empty());
		when(classRoomRepository.save(org.mockito.ArgumentMatchers.any(ClassRoom.class))).thenAnswer(invocation -> invocation.getArgument(0));

		ClassRoomDto result = service.saveClassRoom(dto);

		assertThat(result.getClassName()).isEqualTo("Room 1");
		assertThat(result.getStudentDto()).hasSize(1);
	}

	@Test
	void saveClassRoomRejectsDuplicateNamesAndCapacityOverLimit() {
		ClassRoomDto duplicateDto = classRoomDto(1L);
		when(classRoomRepository.findClassRoomByClassName(duplicateDto.getClassName()))
				.thenReturn(Optional.of(classRoomEntity(5L)));

		assertThatThrownBy(() -> service.saveClassRoom(duplicateDto))
				.isInstanceOf(ApiErrorResponse.class)
				.extracting("errorCode").isEqualTo("1001");

		ClassRoomDto largeDto = classRoomDto(2L);
		List<com.astarel.school.model.dto.StudentDto> students = new ArrayList<>();
		for (long i = 1; i <= 501; i++) {
			students.add(studentDto(i));
		}
		largeDto.setStudentDto(students);
		when(classRoomRepository.findClassRoomByClassName(largeDto.getClassName())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.saveClassRoom(largeDto))
				.isInstanceOf(ApiErrorResponse.class)
				.extracting("errorCode").isEqualTo("1005");
	}

	@Test
	void updateClassRoomPersistsWhenNameBelongsToSameRoom() throws Exception {
		ClassRoomDto dto = classRoomDto(1L);
		dto.setStudentDto(List.of(studentDto(11L)));
		ClassRoom existing = classRoomEntity(1L);
		when(subjectRepository.getSubjectByStudentId(11L)).thenReturn(Optional.of(subjectEntity(21L)));
		when(classRoomRepository.findClassRoomById(1L)).thenReturn(Optional.of(existing));
		when(classRoomRepository.getTotalNumberOfStudentByClassId(1L)).thenReturn(20);
		when(classRoomRepository.findClassRoomByClassName(dto.getClassName())).thenReturn(Optional.of(existing));
		when(classRoomRepository.save(org.mockito.ArgumentMatchers.any(ClassRoom.class))).thenAnswer(invocation -> invocation.getArgument(0));

		ClassRoomDto result = service.updateClassRoom(dto);

		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getStudentDto()).hasSize(1);
	}

	@Test
	void updateClassRoomRejectsCapacityDuplicateAndMissingRoom() {
		ClassRoomDto dto = classRoomDto(1L);
		dto.setStudentDto(List.of(studentDto(11L)));
		when(subjectRepository.getSubjectByStudentId(11L)).thenReturn(Optional.of(subjectEntity(21L)));
		when(classRoomRepository.findClassRoomById(1L)).thenReturn(Optional.of(classRoomEntity(1L)));
		when(classRoomRepository.getTotalNumberOfStudentByClassId(1L)).thenReturn(500);

		assertThatThrownBy(() -> service.updateClassRoom(dto))
				.isInstanceOf(ApiErrorResponse.class)
				.extracting("errorCode").isEqualTo("1005");

		when(classRoomRepository.getTotalNumberOfStudentByClassId(1L)).thenReturn(0);
		ClassRoom other = classRoomEntity(2L);
		other.setClassName(dto.getClassName());
		when(classRoomRepository.findClassRoomByClassName(dto.getClassName())).thenReturn(Optional.of(other));

		assertThatThrownBy(() -> service.updateClassRoom(dto))
				.isInstanceOf(ApiErrorResponse.class)
				.extracting("errorCode").isEqualTo("1003");

		when(classRoomRepository.findClassRoomById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.updateClassRoom(dto))
				.isInstanceOf(ApiErrorResponse.class)
				.extracting("errorCode").isEqualTo("1002");
	}

	@Test
	void deleteClassRoomDelegatesToRepository() {
		service.deleteClassRoomById(7L);

		verify(classRoomRepository).deleteById(7L);
	}
}
