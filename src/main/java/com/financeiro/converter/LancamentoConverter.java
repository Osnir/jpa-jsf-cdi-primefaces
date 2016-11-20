package com.financeiro.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import com.financeiro.model.Lancamento;
import com.financeiro.repository.Lancamentos;
import com.financeiro.util.Util;

@FacesConverter(forClass = Lancamento.class)
public class LancamentoConverter implements Converter {

	@Inject
	private Lancamentos lancamentos;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Lancamento retorno = null;
		if (!Util.isEmptyOrNull(value)) {
			try {
				retorno = this.lancamentos.porId(new Long(value));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Lancamento lancamento = ((Lancamento) value);
			return lancamento.getId() == null ? null : lancamento.getId().toString();
		}
		return null;
	}

}
