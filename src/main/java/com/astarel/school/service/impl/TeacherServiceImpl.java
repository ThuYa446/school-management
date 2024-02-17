package com.astarel.school.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.astarel.school.exception.ApiErrorResponse;
import com.astarel.school.model.dto.StudentDto;
import com.astarel.school.model.dto.SubjectDto;
import com.astarel.school.model.dto.TeacherDto;
import com.astarel.school.model.entity.Student;
import com.astarel.school.model.entity.Subject;
import com.astarel.school.model.entity.Teacher;
import com.astarel.school.repository.TeacherRepository;
import com.astarel.school.service.TeacherService;

@Service
public class TeacherServiceImpl implements TeacherService {

	@Autowired
	TeacherRepository teacherRepository;

	ModelMapper modelMapper = new ModelMapper();

	private List<TeacherDto> teacherListToTeacherDto(List<Teacher> teachers) {
		List<TeacherDto> teacherDto = new ArrayList<TeacherDto>();
		for (Teacher teacher : teachers) {
			teacherDto.add(this.teacherToTeacherDto(teacher));
		}
		return teacherDto;
	}
	
	private TeacherDto teacherToTeacherDto(Teacher teacher) {
		TeacherDto teacherDto = modelMapper.map(teacher, TeacherDto.class);
		if (teacher.getSubject() != null) {
			List<SubjectDto> subjects = this.subjectListToSubjectDto(teacher.getSubject());
			teacherDto.setSubjects(subjects);
		}
		return teacherDto;
	}
	
	private List<StudentDto> studentListToStudentDto(List<Student> students) {
		List<StudentDto> studentDto = new ArrayList<StudentDto>();
		
		for(Student student: students){
			StudentDto stuDto = modelMapper.map(student, StudentDto.class);
			studentDto.add(stuDto);
		}
		return studentDto;
	}

	private List<SubjectDto> subjectListToSubjectDto(List<Subject> subjects) {
		if(subjects != null) {
			List<SubjectDto> subjectDto = new ArrayList<SubjectDto>();
			for (Subject subject : subjects) {
				SubjectDto subjDto = modelMapper.map(subject, SubjectDto.class);
				if(subject.getStudent() != null) {
					subjDto.setStudentDto(studentListToStudentDto(subject.getStudent()));
				}
				subjectDto.add(subjDto);
			}
			return subjectDto;
		}
		return new ArrayList<SubjectDto>();
	}

	private List<Subject> subjectDtoToSubjectList(List<SubjectDto> subjectDtos) {
		if (subjectDtos != null) {
			List<Subject> subjects = new ArrayList<Subject>();
			for (SubjectDto subjectDto : subjectDtos) {
				Subject subject = modelMapper.map(subjectDto, Subject.class);
				subjects.add(subject);
			}
			return subjects;
		}
		return new ArrayList<Subject>();
	}

	@Override
	public List<TeacherDto> getAllTeachers() {
		// TODO Auto-generated method stub
		List<Teacher> teachers = this.teacherRepository.findAll();
		return this.teacherListToTeacherDto(teachers);
	}

	@Override
	public Boolean isExistTeacher(String email) {
		// TODO Auto-generated method stub
		return this.teacherRepository.findTeacherByEmail(email).isPresent();
	}

	@Override
	public Boolean findTeacherById(Long Id) {
		// TODO Auto-generated method stub
		return this.teacherRepository.findTeacherById(Id).isPresent();
	}
	
	@Override
	public TeacherDto getTeacherById(Long id) {
		// TODO Auto-generated method stub
		Teacher teacher = this.teacherRepository.findTeacherById(id).get();
		return this.teacherToTeacherDto(teacher);
	}

	@Override
	public TeacherDto saveTeacher(TeacherDto teacherDto) throws ApiErrorResponse {
		// TODO Auto-generated method stub
		List<Subject> subjects = this.subjectDtoToSubjectList(teacherDto.getSubjects());
		teacherDto.setSubjects(null);
		Teacher teacher = modelMapper.map(teacherDto, Teacher.class);
		if (subjects != null) {
			for (Subject subject : subjects) {
				subject.setTeacher(teacher);
			}
			teacher.setSubject(subjects);
		}
		if (!this.isExistTeacher(teacher.getEmail())) {
			teacher = this.teacherRepository.save(teacher);
		} else {
			throw new ApiErrorResponse("1001", "Teacher already exist");
		}
		TeacherDto teachDto = modelMapper.map(teacher, TeacherDto.class);
		teachDto.setSubjects(this.subjectListToSubjectDto(subjects));
		return teachDto;
	}

	@Override
	public TeacherDto updateTeacher(TeacherDto teacherDto) throws ApiErrorResponse {
		// TODO Auto-generated method stub
		List<Subject> subjects = this.subjectDtoToSubjectList(teacherDto.getSubjects());
		teacherDto.setSubjects(null);
		Teacher teacher = modelMapper.map(teacherDto, Teacher.class);
		if (subjects != null) {
			for (Subject subject : subjects) {
				subject.setTeacher(teacher);
			}
			teacher.setSubject(subjects);
		}
		if (this.findTeacherById(teacher.getId())) {
			if (this.teacherRepository.findTeacherByEmail(teacher.getEmail()).isPresent()) {
				Teacher existedTeacher = this.teacherRepository.findTeacherByEmail(teacher.getEmail()).get();
				if (existedTeacher.isEqualId(teacher.getId())) {
					teacher = this.teacherRepository.save(teacher);
				} else {
					throw new ApiErrorResponse("1003", "Teacher email must be unique.");
				}
			} else {
				throw new ApiErrorResponse("1004", "Invalid email.");
			}
		} else {
			throw new ApiErrorResponse("1002", "No such teacher with id - " + teacherDto.getId() + " found");
		}
		TeacherDto teachDto = modelMapper.map(teacher, TeacherDto.class);
		teachDto.setSubjects(this.subjectListToSubjectDto(subjects));
		return teachDto;
	}

	@Override
	public void deleteTeacherById(Long id) {
		// TODO Auto-generated method stub

	}

}
