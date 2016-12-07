package com.financeiro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;

import com.financeiro.util.Util;
import com.financeiro.validation.DecimalPositivo;

@Entity
@Table(name = "lancamento")
public class Lancamento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "despesa_id")
	private Despesa despesa;	
	@DecimalPositivo
	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal valor;
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoLancamento tipo;
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "data_vencimento", nullable = false)
	private Date dataVencimento;
	@Temporal(TemporalType.DATE)
	@Column(name = "data_pagamento", nullable = true)	
	private Date dataPagamento;
	@Column(name = "nome_comprovante", length = 60, nullable = true)
	private String nomeComprovante;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "comprovante_pagamento", nullable = true)
	private byte[] comprovantePagamento;
	@Formula("(select case isnull(l.data_pagamento) when 1 then 'Pendente' else 'Pago' end from Lancamento l where l.id = id)")
	private String situacao;
	
	public Lancamento() {
		this.tipo = TipoLancamento.DESPESA;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Despesa getDespesa() {
		return despesa;
	}
	public void setDespesa(Despesa despesa) {
		this.despesa = despesa;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public TipoLancamento getTipo() {
		return tipo;
	}
	public void setTipo(TipoLancamento tipo) {
		this.tipo = tipo;
	}
	public Date getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	public Date getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}	
	public String getNomeComprovante() {
		return nomeComprovante;
	}
	public void setNomeComprovante(String nomeComprovante) {
		this.nomeComprovante = nomeComprovante;
	}
	public byte[] getComprovantePagamento() {
		return comprovantePagamento;
	}
	public void setComprovantePagamento(byte[] comprovantePagamento) {
		this.comprovantePagamento = comprovantePagamento;
	}	
	public String getSituacao() {
		return situacao;
	}
//	public void setSituacao(String situacao) {
//		this.situacao = situacao;
//	}

	public boolean dataPagamentoValida() {
		return getDataPagamento() == null || !getDataPagamento().after(new Date());	
	}
	
	public String getNomeArquivo() {
		if (!Util.isEmptyOrNull(this.nomeComprovante)) {
			return this.nomeComprovante.split("\\|")[0];
		}		
		return null;
	}

	public String getContentType() {
		if (!Util.isEmptyOrNull(this.nomeComprovante)) {
			return this.nomeComprovante.split("\\|")[1];
		}		
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lancamento other = (Lancamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
