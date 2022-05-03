package br.com.wepdev.microservicocrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wepdev.microservicocrud.entity.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{

}
