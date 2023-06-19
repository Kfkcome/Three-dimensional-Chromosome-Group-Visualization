package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LastQuery {
    private int cs_id1;
    private int cs_id2;
    private String norms;
    private String binXStart;
    private String binYStart;
    private String binXEnd;
    private String binYEnd;
    private String resolution;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LastQuery lastQuery = (LastQuery) o;
        return cs_id1 == lastQuery.cs_id1 && cs_id2 == lastQuery.cs_id2 && Objects.equals(norms, lastQuery.norms) && Objects.equals(binXStart, lastQuery.binXStart) && Objects.equals(binYStart, lastQuery.binYStart) && Objects.equals(binXEnd, lastQuery.binXEnd) && Objects.equals(binYEnd, lastQuery.binYEnd) && Objects.equals(resolution, lastQuery.resolution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cs_id1, cs_id2, norms, binXStart, binYStart, binXEnd, binYEnd, resolution);
    }
}
