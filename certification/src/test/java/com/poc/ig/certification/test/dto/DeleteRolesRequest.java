package com.poc.ig.certification.test.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter
@Setter
@NoArgsConstructor
public class DeleteRolesRequest implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<String> roles = new ArrayList<String>();

}
