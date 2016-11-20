package com.financeiro.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import com.financeiro.model.Categoria;
import com.financeiro.repository.Categorias;
import com.financeiro.util.Util;

@FacesConverter(forClass = Categoria.class)
public class CategoriaConverter implements Converter {

	 @Inject
	 private Categorias categorias;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Categoria retorno = null;

		if (!Util.isEmptyOrNull(value)) {
			try {
				retorno = categorias.porId(new Long(value));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Categoria categoria = ((Categoria) value);
			return categoria.getId() == null ? null : categoria.getId().toString();
		}

		return null;
	}
}
