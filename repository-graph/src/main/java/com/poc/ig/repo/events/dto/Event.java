package com.poc.ig.repo.events.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.ig.repo.events.Events;
import com.poc.ig.repo.exception.JsonParseException;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
public class Event<T> {   

	private String name;
	private String data;

	public Event(String name, T data, ObjectMapper jsonMapper) {
		super();
		this.name = name;
		if (data instanceof String) {
			this.data = (String) data;
		} else {
			try {
				this.data = jsonMapper.writeValueAsString(data);
			} catch (JsonProcessingException e) {
				e.printStackTrace(); // TODO
				throw new JsonParseException(e);
			}
		}

	}

	public Object getEventData(ObjectMapper jsonMapper) {
		Object result = null;
		if (name.equals(Events.NEW_CERTITICATION_EVENT)) {
			try {
				result = jsonMapper.readValue(data, CertificationDto.class);
			} catch (JsonProcessingException e) {
				e.printStackTrace(); //TODO
				throw new JsonParseException(e);
			}
		}
		return result;
	}
	
	public T getEventData(ObjectMapper jsonMapper, Class<T> valueType) {
		T result ;
		try {
			result = jsonMapper.readValue(data, valueType);
		} catch (JsonProcessingException e) {
			e.printStackTrace(); // TODO
			throw new JsonParseException(e);
		}
		return result;
	}


}
