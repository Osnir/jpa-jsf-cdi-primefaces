package com.financeiro.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import com.financeiro.model.Despesa;
import com.financeiro.repository.Despesas;
import com.financeiro.util.Util;

@FacesConverter(forClass = Despesa.class)
public class DespesaConverter implements Converter {

	@Inject
	private Despesas despesas;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Despesa retorno = null;

		if (!Util.isEmptyOrNull(value)) {
			try {
				retorno = despesas.porId(new Long(value));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Despesa despesa = ((Despesa) value);
			return despesa.getId() == null ? null : despesa.getId().toString();
		}

		return null;
	}
}
