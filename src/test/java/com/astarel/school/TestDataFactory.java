package com.astarel.school;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.astarel.school.model.dto.ClassRoomDto;
import com.astarel.school.model.dto.StudentDto;
import com.astarel.school.model.dto.SubjectDto;
import com.astarel.school.model.dto.TeacherDto;
import com.astarel.school.model.entity.ClassRoom;
import com.astarel.school.model.entity.Role;
import com.astarel.school.model.entity.Student;
import com.astarel.school.model.entity.StudentType;
import com.astarel.school.model.entity.Subject;
import com.astarel.school.model.entity.Teacher;
import com.astarel.school.model.entity.User;

public final class TestDataFactory {

	private TestDataFactory() {
	}

	public static StudentDto studentDto(Long id) {
		StudentDto dto = new StudentDto();
		dto.setId(id);
		dto.setName("Student " + id);
		dto.setEmail("student" + id + "@school.com");
		dto.setPhoneNo("09123456" + id);
		dto.setAddress("Address " + id);
		dto.setStudentType(StudentType.Internal);
		return dto;
	}

	public static Student studentEntity(Long id) {
		Student student = new Student();
		student.setId(id);
		student.setName("Student " + id);
		student.setEmail("student" + id + "@school.com");
		student.setPhoneNo("09123456" + id);
		student.setAddress("Address " + id);
		student.setStudentType(StudentType.Internal);
		return student;
	}

	public static SubjectDto subjectDto(Long id) {
		SubjectDto dto = new SubjectDto();
		dto.setId(id);
		dto.setTitle("Subject " + id);
		return dto;
	}

	public static Subject subjectEntity(Long id) {
		Subject subject = new Subject();
		subject.setId(id);
		subject.setTitle("Subject " + id);
		return subject;
	}

	public static TeacherDto teacherDto(Long id) {
		TeacherDto dto = new TeacherDto();
		dto.setId(id);
		dto.setName("Teacher " + id);
		dto.setEmail("teacher" + id + "@school.com");
		dto.setPhoneNo("09999888" + id);
		dto.setAddress("Teacher Address " + id);
		return dto;
	}

	public static Teacher teacherEntity(Long id) {
		Teacher teacher = new Teacher();
		teacher.setId(id);
		teacher.setName("Teacher " + id);
		teacher.setEmail("teacher" + id + "@school.com");
		teacher.setPhoneNo("09999888" + id);
		teacher.setAddress("Teacher Address " + id);
		return teacher;
	}

	public static ClassRoomDto classRoomDto(Long id) {
		ClassRoomDto dto = new ClassRoomDto();
		dto.setId(id);
		dto.setClassName("Room " + id);
		return dto;
	}

	public static ClassRoom classRoomEntity(Long id) {
		ClassRoom classRoom = new ClassRoom();
		classRoom.setId(id);
		classRoom.setClassName("Room " + id);
		return classRoom;
	}

	public static User userEntity(String email, String password, String... roleNames) {
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setRoles(new HashSet<>());
		Arrays.stream(roleNames).forEach(roleName -> {
			Role role = new Role();
			role.setRole(roleName);
			role.setUser(user);
			user.getRoles().add(role);
		});
		return user;
	}

	public static List<StudentDto> studentDtos(Long... ids) {
		return Arrays.stream(ids).map(TestDataFactory::studentDto).toList();
	}

	public static List<SubjectDto> subjectDtos(Long... ids) {
		return Arrays.stream(ids).map(TestDataFactory::subjectDto).toList();
	}
}
