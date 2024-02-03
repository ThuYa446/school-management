package com.astarel.school.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.astarel.school.model.dto.TeacherDto;
import com.astarel.school.model.entity.Teacher;
import com.astarel.school.repository.TeacherRepository;
import com.astarel.school.service.TeacherService;

@Service
public class TeacherServiceImpl implements TeacherService{
	
	@Autowired
	TeacherRepository teacherRepository;
	
	ModelMapper modelMapper = new ModelMapper();

	@Override
	public TeacherDto saveTeacher(TeacherDto teacherDto) {
		// TODO Auto-generated method stub
		
		Teacher teacher = modelMapper.map(teacherDto, Teacher.class);
		
		teacher = this.teacherRepository.save(teacher);
		
		TeacherDto teacDto = modelMapper.map(teacher, TeacherDto.class) ;
		
		return teacDto;
	}

}
