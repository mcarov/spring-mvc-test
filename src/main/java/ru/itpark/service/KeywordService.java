package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itpark.domain.Keyword;
import ru.itpark.repository.KeywordRepository;

import java.util.List;

import static ru.itpark.Constants.LIST_SIZE;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;

    public long getKeywordRepoSize() {
        return keywordRepository.size();
    }

    public List<Keyword> getCollections(int number) {
        int offset = LIST_SIZE*(number-1);
        return keywordRepository.getKeywords(offset);
    }

    public Keyword getKeywordById(long id) {
        return keywordRepository.getKeywordById(id);
    }

    long getKeywordIdByName(String name) {
        return  keywordRepository.getKeywordIdByName(name);
    }

    void saveKeyword(Keyword keyword) {
        keywordRepository.saveKeyword(keyword);
    }

    void removeKeywordById(long id) {
        keywordRepository.removeKeywordById(id);
    }
}
