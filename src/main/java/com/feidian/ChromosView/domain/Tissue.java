package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 组织
 *
 * @author new
 * @date 2023/08/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tissue {
    /**
     * 组织id
     */
    int TISSUE_ID;
    /**
     * 组织名字
     */
    String TISSUE_NAME;
}
