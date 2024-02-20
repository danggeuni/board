package com.board.board.controller;

import com.board.board.controller.dto.board.AddArticleRequest;
import com.board.board.controller.dto.board.GetArticleResponse;
import com.board.board.controller.dto.board.PagingResponse;
import com.board.board.domain.entity.BoardEntity;
import com.board.board.domain.entity.FileEntity;
import com.board.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final HttpSession session;
    private List<GetArticleResponse> recentlyData = new ArrayList<>();

    @Autowired
    public BoardController(BoardService boardService, HttpSession session) {
        this.boardService = boardService;
        this.session = session;
    }

    @GetMapping
    public String articleList(Model model, @RequestParam(defaultValue = "15") int pageSize,
                              @RequestParam(defaultValue = "1") int pageNumber, @RequestParam(required = false) String search,
                              HttpSession session) {
        PagingResponse<BoardEntity> response = boardService.getArticles(pageSize, pageNumber, search);

        List<GetArticleResponse> list = response.getData().stream().
                map(GetArticleResponse::new).
                collect(Collectors.toList());

        ;

        model.addAttribute("list", list);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", response.getTotalPages());
        model.addAttribute("search", search);
        model.addAttribute("userId", session.getAttribute("userId"));

        return "board/articleList";
    }

    @GetMapping("/{id}")
    public String viewArticle(Model model, @PathVariable Long id) {
        BoardEntity entity = boardService.findArticleById(id);
        GetArticleResponse data = new GetArticleResponse(entity);

        // 최근 본 목록 데이터 생성
        if (session.getAttribute("recentlyView") == null) {
            recentlyData = new ArrayList<>();
        }

        recentlyData.add(data);
        session.setAttribute("recentlyView", recentlyData);
        model.addAttribute("data", data);
        model.addAttribute("id", id);

        // 첨부파일 확인
        FileEntity file = boardService.getFileById(id);
        if (file != null) {
            model.addAttribute("file", file);
        }

        return "board/view";
    }

    @GetMapping(path = {"/new", "/{id}/reply"})
    public String createArticle(Model model, @PathVariable(required = false) Long id, @ModelAttribute AddArticleRequest request) {
        model.addAttribute("data", new AddArticleRequest());
        model.addAttribute("id", id);
        model.addAttribute("name", session.getAttribute("userId"));
        return "board/new";
    }

    @PostMapping("/new")
    public String newArticle(@ModelAttribute AddArticleRequest request, Long id, @RequestParam MultipartFile file) {
        boardService.insertArticle(request, id);

        if (!file.isEmpty()) {
            try {
                File uploadPath = new File("C:/temp");

                if (!uploadPath.exists()) uploadPath.mkdirs();

                String originName = file.getOriginalFilename();
                String saveName = UUID.randomUUID() + "_" + originName;

                File destination = new File(uploadPath + "/" + saveName);
                file.transferTo(destination);

                Long lastId = boardService.getLastArticle().getId();
                boardService.insertFile(uploadPath.toString(), originName, saveName, lastId);
            } catch (IOException e) {
                throw new RuntimeException("첨부 파일 업로드에 실패하였습니다.");
            }
        }
        return "redirect:/board";
    }
    @GetMapping("/download/{downName}")
    public void downloadFile(@PathVariable String downName, HttpServletResponse response) {

        try {
            String filePath = "C:/Temp/" + downName;
            InputStream inputStream = new FileInputStream(filePath);

            response.setContentType("application/octet-stream");

            OutputStream outputStream = response.getOutputStream();

            byte[] data = new byte[1024];

            while (true) {
                int num = inputStream.read(data);
                outputStream.write(data, 0, num);
                if (num == -1) break;
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/recently-view")
    public String recentlyView(Model model) {
        model.addAttribute("userId", session.getAttribute("userId"));
        model.addAttribute("list", session.getAttribute("recentlyView"));
        return "board/recentlyView";
    }
}
