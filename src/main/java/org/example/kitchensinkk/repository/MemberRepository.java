package org.example.kitchensinkk.repository;

import org.example.kitchensinkk.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<Member, String> {
    Member findByEmail(String email);
}
