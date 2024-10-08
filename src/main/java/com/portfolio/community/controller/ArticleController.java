package com.portfolio.community.controller;

import com.portfolio.community.dto.ArticleForm;
import com.portfolio.community.entitiy.Article;
import com.portfolio.community.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;


@Slf4j
@Controller
public class ArticleController {
    @Autowired // 스프링 부트가 미리 생성해 놓은 객체, 의존성 주입
    private ArticleRepository articleRepository; // 객체 선언


    @GetMapping("/articles/new")
    public String newArticleForm() {
        return "articles/new";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form) {
        log.info(form.toString());
        // 앞으로 할 작업
        // 1. DTO를 entitiy로 변환
        // 2. Repository로 entitiy를 DB에 저장
            Article article = form.toEntity();
            log.info(article.toString());

            Article saved = articleRepository.save(article);
            log.info(saved.toString());

          return "redirect:/articles/" + saved.getId();
        //return "redirect:/articles";
    }

    @GetMapping("articles/{id}") // 데이터 요청 접수
    public String show(@PathVariable Long id, Model model) {
        // @PathVariable : URL 요청으로 들어온 전달값을 컨트롤러의 매개변수로 가져온다. (Long id를 {id}로)
        log.info("id: " + id);
        // 0. 클라이언트가 접속하는 URL 요청을 컨트롤러에서 받기 ("article -> log print)
        // 1. 해당하는 아이디를 조회해 데이터를 가져오기 : Repository
        Article articleEntity = articleRepository.findById(id).orElse(null);
        // 2. 모델에 데이터 등록하기
        model.addAttribute("article", articleEntity);
        // 3. 뷰 페이지 제작하여 반환하기
        return "articles/show";
    }

    @GetMapping("/articles")
    public String index(Model model){
        // 1. 모든 데이터 가져오기
        ArrayList<Article> articleEntityList = articleRepository.findAll();
        // 2. 모델에 데이터 등록하기
        model.addAttribute("articleList", articleEntityList);
        // 3. 뷰페이지 설정하기
        return "articles/index";
    }

    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {// 모델 객체 받아오기
        // 수정할 데이터 가져오가
        Article articleEntity = articleRepository.findById(id).orElse(null);
        // 모델에 데이터 등록하기
        model.addAttribute("article", articleEntity); // 아티클 엔티티를 아티클로 등록
        // 뷰 페이지 설정하기
        return "articles/edit";
    }

    @PostMapping("/articles/update")
    public String update(ArticleForm form) {

        //1. DTO 를 엔티티로 2. 엔티티를 디비에 3. 수정된 결과 페이지로 리다이렉트 시키기
        Article articleEntity = form.toEntity();
        // 기존 데이터 가져오기
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
        // 갱신하기
        if(target != null) {
            articleRepository.save(articleEntity);
        }
        return "redirect:/articles/" + articleEntity.getId();
    }
}
