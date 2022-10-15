package com.weesftw.adapter.listener.http.wrapper;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.weesftw.api.model.request.CreateNewsRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WrapperNewsResponse {

    @JsonAlias({"articles"})
    private List<CreateNewsRequest> articles;
}