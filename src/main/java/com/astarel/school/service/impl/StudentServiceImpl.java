package com.astarel.school.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.astarel.school.exception.ApiErrorResponse;
import com.astarel.school.model.dto.StudentDto;
import com.astarel.school.model.entity.Student;
import com.astarel.school.repository.StudentRepository;
import com.astarel.school.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService{
	
	@Autowired
	StudentRepository studentRepository;
	
	ModelMapper modelMapper = new ModelMapper();
	
	private List<StudentDto> studentListToStudentDto(List<Student> students) {
		List<StudentDto> studentDto = new ArrayList<StudentDto>();
		
		for(Student student: students){
			StudentDto stuDto = modelMapper.map(student, StudentDto.class);
			studentDto.add(stuDto);
		}
		return studentDto;
	}
	
	@Override
	public List<StudentDto> getAllStudents() {
		// TODO Auto-generated method stub
		List<Student> students = this.studentRepository.findAll();
		return this.studentListToStudentDto(students);
	}
	
	@Override
	public List<StudentDto> getStudentNotEnrolledSubject() {
		// TODO Auto-generated method stub
		List<Student> students = this.studentRepository.findBySubjectIsNull();
		return this.studentListToStudentDto(students);
	}
	
	@Override
	public List<StudentDto> getStudentNotJoinedClass() {
		// TODO Auto-generated method stub
		List<Student> students = this.studentRepository.findByClassRoomIsNull();
		return this.studentListToStudentDto(students);
	}

	@Override
	public Boolean isExistStudent(String email) {
		// TODO Auto-generated method stub
		return this.studentRepository.findStudentByEmail(email).isPresent();
	}

	@Override
	public Boolean findStudetById(Long Id) {
		// TODO Auto-generated method stub
		return this.studentRepository.findStudentById(Id).isPresent();
	}
	
	@Override
	public StudentDto getStudentById(Long id) {
		// TODO Auto-generated method stub
		Student student = this.studentRepository.findStudentById(id).get();
		return modelMapper.map(student, StudentDto.class);
	}

	@Override
	public StudentDto saveStudent(StudentDto studentDto) throws ApiErrorResponse {
		// TODO Auto-generated method stub
		
		Student student = modelMapper.map(studentDto, Student.class);
		if(!this.isExistStudent(student.getEmail())) {
			student = this.studentRepository.save(student);
		}else {
			throw new ApiErrorResponse("1001", "Student already exist");
		}
		StudentDto stuDto = modelMapper.map(student, StudentDto.class) ;
		
		return stuDto;
	}

	@Override
	public StudentDto updateStudent(StudentDto studetnDto) throws ApiErrorResponse {
		// TODO Auto-generated method stub
		Student student = modelMapper.map(studetnDto, Student.class);
		if (this.findStudetById(student.getId())) {
			if(this.studentRepository.findStudentByEmail(student.getEmail()).isPresent()) {
				Student existedStudent = this.studentRepository.findStudentByEmail(student.getEmail()).get();
				if(existedStudent.isEqualId(student.getId())) {
					student = this.studentRepository.save(student);
				}else {
					throw new ApiErrorResponse("1003", "Student email must be unique.");
				}
			} else {
				throw new ApiErrorResponse("1004", "Invalid email.");
			}
		} else {
			throw new ApiErrorResponse("1002", "No such student with id - "+studetnDto.getId()+" found");
		}
		StudentDto stuDto = modelMapper.map(student, StudentDto.class);
		return stuDto;
	}

	@Override
	public void deleteStudentById(Long id) {
		// TODO Auto-generated method stub
		this.studentRepository.deleteById(id);
	}

	
}
