package br.com.wepdev.microservicocrud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wepdev.microservicocrud.data.vo.ProdutoVO;
import br.com.wepdev.microservicocrud.repository.ProdutoRepository;

@Service
public class ProdutoService {
	
	private final ProdutoRepository produtoRepository;

	
	@Autowired
	public ProdutoService(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}
	
	
	public ProdutoVO create(ProdutoVO produto) {
		return null;
	}
	
	

}
