package com.example.bankcards.dto.block;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BlockRequestCreate {
    @NotNull private Long cardId;
    @Size(max = 255) private String comment;
}
