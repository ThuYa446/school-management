package com.astarel.school.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.astarel.school.exception.ApiErrorResponse;
import com.astarel.school.model.dto.TeacherDto;
import com.astarel.school.model.entity.Teacher;
import com.astarel.school.repository.TeacherRepository;
import com.astarel.school.service.TeacherService;

@Service
public class TeacherServiceImpl implements TeacherService{
	
	@Autowired
	TeacherRepository teacherRepository;
	
	ModelMapper modelMapper = new ModelMapper();
	
	private List<TeacherDto> teacherListToTeacherDto(List<Teacher> teachers) {
		List<TeacherDto> teacherDto = new ArrayList<TeacherDto>();
		
		for(Teacher teacher: teachers){
			TeacherDto teachDto = modelMapper.map(teacher, TeacherDto.class);
			teacherDto.add(teachDto);
		}
		return teacherDto;
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
	public TeacherDto saveTeacher(TeacherDto teacherDto) throws ApiErrorResponse {
		// TODO Auto-generated method stub
		Teacher teacher = modelMapper.map(teacherDto, Teacher.class);
		if(!this.isExistTeacher(teacher.getEmail())) {
			teacher = this.teacherRepository.save(teacher);
		}else {
			throw new ApiErrorResponse("1001", "Teacher already exist");
		}
		TeacherDto teachDto = modelMapper.map(teacher, TeacherDto.class) ;
		
		return teachDto;
	}

	@Override
	public TeacherDto updateTeacher(TeacherDto teacherDto) throws ApiErrorResponse {
		// TODO Auto-generated method stub
		Teacher teacher = modelMapper.map(teacherDto, Teacher.class);
		if (this.findTeacherById(teacher.getId())) {
			if(this.teacherRepository.findTeacherByEmail(teacher.getEmail()).isPresent()) {
				Teacher existedTeacher = this.teacherRepository.findTeacherByEmail(teacher.getEmail()).get();
				if(existedTeacher.isEqualId(teacher.getId())) {
					teacher = this.teacherRepository.save(teacher);
				}else {
					throw new ApiErrorResponse("1003", "Teacher email must be unique.");
				}
			} else {
				throw new ApiErrorResponse("1004", "Invalid email.");
			}
		} else {
			throw new ApiErrorResponse("1002", "No such teacher with id - "+teacherDto.getId()+" found");
		}
		TeacherDto teachDto = modelMapper.map(teacher, TeacherDto.class);
		return teachDto;
	}

	@Override
	public void deleteTeacherById(Long id) {
		// TODO Auto-generated method stub
		
	}



}
