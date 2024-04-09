package daelim.learning.reply.service;

import daelim.learning.board.Board;
import daelim.learning.board.BoardRepository;
import daelim.learning.reply.Reply;
import daelim.learning.reply.ReplyRepository;
import daelim.learning.reply.dto.ReplyListResponse;
import daelim.learning.reply.dto.ReplyRequest;
import daelim.learning.user.User;
import daelim.learning.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyService {

    private final ReplyRepository replyRepository;

    public void save(ReplyRequest replyRequest, Board board, User user) {
        Reply reply = replyRequest.toEntity(board, user);
        replyRepository.save(reply);
    }

    public List<ReplyListResponse> findAll() {

        return replyRepository.findAll().stream().map(
                reply -> ReplyListResponse.builder()
                        .comment(reply.getComment())
                        .user(reply.getUserNo())
                        .build()
        ).toList();
    }

    public void remove(Long id) {
        replyRepository.deleteById(id);
    }
}
