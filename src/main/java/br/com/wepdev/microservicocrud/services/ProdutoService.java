package br.com.wepdev.microservicocrud.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.wepdev.microservicocrud.data.vo.ProdutoVO;
import br.com.wepdev.microservicocrud.entity.Produto;
import br.com.wepdev.microservicocrud.exception.ResourceNotFoundException;
import br.com.wepdev.microservicocrud.message.ProdutoSendMessage;
import br.com.wepdev.microservicocrud.repository.ProdutoRepository;

@Service
public class ProdutoService {
	
	private final ProdutoRepository produtoRepository;
	private final ProdutoSendMessage produtoSendMessage;

	
	@Autowired
	public ProdutoService(ProdutoRepository produtoRepository, ProdutoSendMessage produtoSendMessage) {
		this.produtoRepository = produtoRepository;
		this.produtoSendMessage = produtoSendMessage;
	}
	
	
	public ProdutoVO create(ProdutoVO produtoVO) {
		ProdutoVO proVoRetorno = ProdutoVO.converteProdutoParaProdutoVO(
				produtoRepository.save(Produto.converteProdutoVOParaProduto(produtoVO)));
		
		produtoSendMessage.sendMessage(proVoRetorno); // Eviando o produto atraves da fila para atualizar o micrserviço de Pagamento
		
		return proVoRetorno;
	}
	
	
	public Page<ProdutoVO> findAll(Pageable pageable){
		var page = produtoRepository.findAll(pageable);
		return page.map(this::convertToProdutoVO);
		
	}
	
	private ProdutoVO convertToProdutoVO(Produto produto) {
		return ProdutoVO.converteProdutoParaProdutoVO(produto);
	}
	
	
	public ProdutoVO findById(Long id) {
		var entity = produtoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		return ProdutoVO.converteProdutoParaProdutoVO(entity);
	}
	
	
	public ProdutoVO update(ProdutoVO produtoVO) {
	    final Optional<Produto> optionalProduto = produtoRepository.findById(produtoVO.getId());
		if(optionalProduto.isPresent()) {
			new ResourceNotFoundException("No records found for this ID");
		}
		return ProdutoVO.converteProdutoParaProdutoVO(produtoRepository.save(Produto.converteProdutoVOParaProduto(produtoVO)));
	}
	
	
	public void delete(Long id) {
		var entity = produtoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
			produtoRepository.delete(entity);
	}
	


}
