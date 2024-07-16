package com.literalura.literalura.model;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro<DatosAutor>(
        @JsonAlias("id") Long id,
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DatosAutor> autores,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("copyright") String copyright,
        @JsonAlias("download_count") Integer descargas) {
}