package com.financeiro.controller.bean;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.financeiro.model.Despesa;
import com.financeiro.model.Lancamento;
import com.financeiro.model.TipoLancamento;
import com.financeiro.repository.Despesas;
import com.financeiro.repository.Lancamentos;
import com.financeiro.service.BusinessException;
import com.financeiro.service.LancamentoService;

@Named
@javax.faces.view.ViewScoped
public class LancamentoBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	@Inject
	private Lancamentos repository;
	@Inject
	private Despesas despesaRepository;
	@Inject
	private LancamentoService service;

	private Lancamento lancamento;
	private List<Lancamento> lancamentos;
	private StreamedContent fileDownload;
   
	public String novoLancamento() {
		return "/pages/interno/CadastroLancamento?faces-redirect=true";
	}

	public void prepararCadastro() {
		if (this.lancamento == null) {
			this.lancamento = new Lancamento();
		}
	}

	public void salvar() {
		try {
			this.lancamento = this.service.Salvar(lancamento);
			this.addInfoMessage("Lançamento salvo com sucesso!");
		} catch (BusinessException e) {
			this.addErrorMessage(e);
		} catch (Exception e) {
			this.addErrorMessage("Erro ao salvar registro.");
		}
	}

	public void salvarNovo() {
		try {
			this.service.Salvar(lancamento);
			this.lancamento = new Lancamento();
			this.addInfoMessage("Lançamento salvo com sucesso!");
		} catch (BusinessException ex) {
			this.addErrorMessage(ex);
		} catch (Exception e) {
			this.addErrorMessage("Erro ao salvar registro.");
		}
	}

	public void excluir() {
		try {
			this.service.excluir(this.lancamento);
			this.lancamento = new Lancamento();
			this.consultar();
		} catch (BusinessException e) {
			this.addErrorMessage(e);
		} catch (Exception e) {
			this.addErrorMessage("Erro ao excluir registro.");
		}
	}

	public void consultar() {
		try {
			this.lancamentos = this.repository.todos();
		} catch (Exception e) {
			this.addErrorMessage("Erro ao carregar registros.");
		}
	}

	public void pesquisar() {
		try {
			this.lancamentos = repository.filtrar(this.getFiltro());
		} catch (Exception e) {
			this.addErrorMessage("Erro ao pesquisar registros.");
		}
	}

	public List<Despesa> pesquisarDespesas(String nome) {
		try {
			return this.despesaRepository.porParteNome(nome);
		} catch (Exception e) {
			this.addErrorMessage("Erro ao carregar despesas.");
			return null;
		}
	}

	public void comprovanteListener(FileUploadEvent event) {
		UploadedFile uploadedFile = event.getFile();
		String nome = uploadedFile.getFileName() + "|" +  uploadedFile.getContentType();
		
		try {
			this.lancamento.setNomeComprovante(nome);
			this.lancamento.setComprovantePagamento(uploadedFile.getContents());
		} catch (Exception e) {
			this.addErrorMessage("Erro ao carregar comprovante de pagamento.");
		}
	}
	
	public void downloadComprovante() {
		this.fileDownload = new DefaultStreamedContent(new ByteArrayInputStream(
				this.lancamento.getComprovantePagamento()), 
				this.lancamento.getContentType(), 
				this.lancamento.getNomeArquivo());
	}
	
	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public TipoLancamento[] getTiposLancamentos() {
		return TipoLancamento.values();
	}

	public Lancamento getLancamento() {
		return lancamento;
	}
	
	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}
	
	public StreamedContent getFileDownload() {
		return fileDownload;
	}
}
