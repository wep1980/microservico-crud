package br.com.wepdev.microservicocrud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.wepdev.microservicocrud.data.vo.ProdutoVO;
import br.com.wepdev.microservicocrud.services.ProdutoService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;;

@RestController
@RequestMapping("/produto")
public class ProdutoController {
	
	private final ProdutoService produtoService;
	private final PagedResourcesAssembler<ProdutoVO> assembler;
	
	
	@Autowired
	public ProdutoController(ProdutoService produtoService, PagedResourcesAssembler<ProdutoVO> assembler) {
		this.produtoService = produtoService;
		this.assembler = assembler;
	}
	
	
	@GetMapping(value = "/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
	public ProdutoVO findById(@PathVariable("id") Long id) {
		
		ProdutoVO produtoVO = produtoService.findById(id);
		produtoVO.add(linkTo(methodOn(ProdutoController.class).findById(id)).withSelfRel()); // Link para o recurso, aparece na representação
		
		return produtoVO;
	}
	
	
	/**
	 * @RequestParam -> não é obrigatorio passar o parametro na requisição.
	 *
	 */
	@GetMapping(produces = {"application/json", "application/xml", "application/x-yaml"})
	public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
			                         @RequestParam(value = "limit", defaultValue = "12") int limit,
			                         @RequestParam(value = "direction", defaultValue = "asc") String direction) {
		
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC; // if ternario, se o que veio por parametro for igual a desc, ordem decrescente, senão ordem normal
		
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "nome")); // define o numero da pagina, tamanho de elementos por pagina, e a direção de ordenação que é feita por nome
		
		Page<ProdutoVO> produtos = produtoService.findAll(pageable); // Buscando os produtos
		
		produtos.stream().forEach(p -> p.add(linkTo(methodOn(ProdutoController.class).findById(p.getId())).withSelfRel())); // links de recursos para cada produto
		
		PagedModel<EntityModel<ProdutoVO>> pagedModel = assembler.toModel(produtos); // retorna o link de cada pagina com todas as informações
		
		return new ResponseEntity<>(pagedModel, HttpStatus.OK);
	}
	
	
	@PostMapping(produces = {"application/json", "application/xml", "application/x-yaml"}, 
			consumes = {"application/json", "application/xml", "application/x-yaml"})
	public ProdutoVO create(@RequestBody ProdutoVO produtoVO) {
		ProdutoVO proVO = produtoService.create(produtoVO);
		proVO.add(linkTo(methodOn(ProdutoController.class).findById(proVO.getId())).withSelfRel()); // Link para o recurso, aparece na representação
		return proVO;
	}
	
	
	@PutMapping(produces = {"application/json", "application/xml", "application/x-yaml"}, 
			consumes = {"application/json", "application/xml", "application/x-yaml"})
	public ProdutoVO update(@RequestBody ProdutoVO produtoVO) {
		ProdutoVO proVO = produtoService.update(produtoVO);
		produtoVO.add(linkTo(methodOn(ProdutoController.class).findById(produtoVO.getId())).withSelfRel()); // Link para o recurso, aparece na representação
		return proVO;
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		produtoService.delete(id);
		return ResponseEntity.ok().build();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
