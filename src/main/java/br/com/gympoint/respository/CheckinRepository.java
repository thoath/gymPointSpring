package br.com.gympoint.respository;

import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.gympoint.schemas.Checkins;

public interface CheckinRepository extends MongoRepository<Checkins, String>{

	Set<Checkins> findByStudent(Integer studentId);
}
