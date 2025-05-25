package com.reciperestapi.reciperestapi.security.repository;

import com.reciperestapi.reciperestapi.security.model.Token;

import java.util.List;
import java.util.Optional;

public interface TokenDAO {
    List<Token> findAllValidTokenByUserId(Integer id);
    Optional<Token> findByToken(String token);
    void saveToken(Token token);
    void update(List<Token> validUserTokens);
   void revokeToken();
   void revokeAllToken();
}
