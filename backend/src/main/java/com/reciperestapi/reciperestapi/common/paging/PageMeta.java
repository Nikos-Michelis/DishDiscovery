package com.reciperestapi.reciperestapi.common.paging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageMeta implements Serializable{
    private int currentRows;
    private int totalRows;
    private int rowsRemaining;
    private int totalPages;
    private int pagesRemaining;
    private String nextPage;
    private int nextPageOffset;
}