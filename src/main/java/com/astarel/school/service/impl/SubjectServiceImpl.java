package com.astarel.school.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.astarel.school.model.dto.SubjectDto;
import com.astarel.school.model.entity.Subject;
import com.astarel.school.repository.SubjectRepository;
import com.astarel.school.service.SubjectService;

@Service
public class SubjectServiceImpl implements SubjectService{
	
	@Autowired
	SubjectRepository subjectRepository;
	
	ModelMapper modelMapper = new ModelMapper();

	@Override
	public SubjectDto saveSubject(SubjectDto subjectDto) {
		// TODO Auto-generated method stub
		Subject subject = modelMapper.map(subjectDto, Subject.class);
		
		subject = this.subjectRepository.save(subject);
		
		SubjectDto subjtDto = modelMapper.map(subject, SubjectDto.class) ;
		
		return subjtDto;
	}

}
