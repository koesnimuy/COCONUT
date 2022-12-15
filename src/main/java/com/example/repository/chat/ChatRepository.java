package com.example.repository.chat;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Chat;
import com.example.entity.Member;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    public List<Chat> findBySenderOrderByChatnoDesc(String sender, Pageable pageable);
    public List<Chat> findByReceiverOrderByChatnoDesc(String receiver, Pageable pageable);
    public int countBySender(String sender);
    public int countByReceiver(String receiver);
    List<Chat> findByMember(Member member);

    public Chat findByChatno(Long chatno);
}
