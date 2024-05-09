package daelim.learning.like;

import daelim.learning.board.Board;
import daelim.learning.board.BoardRepository;
import daelim.learning.like.dto.LikeRequest;
import daelim.learning.user.User;
import daelim.learning.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public String addLike(LikeRequest request) {
        User user = userRepository.findById(request.getUserNo())
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
        Board board = boardRepository.findById(request.getBoardNo())
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));

        Like existingLike = likeRepository.findByUserAndBoard(user, board);

        if (existingLike != null) {
            // 이미 찜한 상태이면 찜 해제
            likeRepository.delete(existingLike);
            return "removed"; // 해제하면 removed 반환
        } else {
            // 찜하지 않은 상태이면 찜 추가
            likeRepository.save(request.toEntity(user, board));
            return "added"; // 추가하면 added 반환
        }
    }

    public List<Board> likeBoardlist(Long userNo) {
        // 사용자가 찜한 게시글의 ID 목록을 가져옵니다.
        List<Like> likedBoards = likeRepository.findByUser_UserNo(userNo);
        // 게시글 ID 목록으로 해당하는 게시글들을 조회합니다.

        List<Long> likedBoardIds = likedBoards.stream()
                .map(like -> like.getBoard().getBoardNo())
                .collect(Collectors.toList());
        return boardRepository.findAllByBoardNoIn(likedBoardIds);

    }
}