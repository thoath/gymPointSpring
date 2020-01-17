package br.com.gympoint.service;

import java.util.List;

import br.com.gympoint.dto.CheckinDto;
import br.com.gympoint.schemas.Checkins;

public interface CheckinService {
	
	CheckinDto store(Checkins checkin);
	List<CheckinDto> show(Integer studentId);
}
