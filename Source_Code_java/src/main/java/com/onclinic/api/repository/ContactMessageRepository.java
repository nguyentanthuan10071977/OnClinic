package com.onclinic.api.repository;
import com.onclinic.api.model.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {}
