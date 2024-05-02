package com.example.data.domain

import com.example.domain.domain.CharacterModelBo

private const val DEFAULT_STRING_VALUE = "No hay información disponible de este personaje"

fun List<CharacterModelDto>.listDtoToBo() = map{it.dtoToBo()}

fun CharacterModelDto.dtoToBo() = CharacterModelBo(
    id = id,
    name = name,
    description = description.ifEmpty { DEFAULT_STRING_VALUE },
    imageURL = thumbnail.getUrl(),
    favorite = false
)
