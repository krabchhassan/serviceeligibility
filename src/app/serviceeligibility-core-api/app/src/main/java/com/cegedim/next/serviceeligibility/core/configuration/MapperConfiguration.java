package com.cegedim.next.serviceeligibility.core.configuration;

import com.cegedim.next.serviceeligibility.core.features.almerysProductRef.AlmerysProductMapper;
import com.cegedim.next.serviceeligibility.core.features.almerysProductRef.AlmerysProductMapperImpl;
import com.cegedim.next.serviceeligibility.core.features.consultationdroits.IDBCLCStructMapper;
import com.cegedim.next.serviceeligibility.core.features.consultationdroits.IDBCLCStructMapperImpl;
import com.cegedim.next.serviceeligibility.core.mapper.MapperContractTPMaille;
import com.cegedim.next.serviceeligibility.core.mapper.MapperContractTPMailleImpl;
import com.cegedim.next.serviceeligibility.core.soap.carte.mapper.CarteDematMapper;
import com.cegedim.next.serviceeligibility.core.soap.carte.mapper.CarteDematMapperImpl;
import com.cegedim.next.serviceeligibility.core.soap.consultation.mapper.BaseDroitMapper;
import com.cegedim.next.serviceeligibility.core.soap.consultation.mapper.BaseDroitMapperImpl;
import com.cegedim.next.serviceeligibility.core.soap.consultation.mapper.ContractToDeclarationMapper;
import com.cegedim.next.serviceeligibility.core.soap.consultation.mapper.ContractToDeclarationMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

  @Bean
  public ContractToDeclarationMapper contractToDeclarationMapper() {
    return new ContractToDeclarationMapperImpl();
  }

  @Bean
  public BaseDroitMapper baseDroitMapper() {
    return new BaseDroitMapperImpl();
  }

  @Bean
  public CarteDematMapper carteDematMapper() {
    return new CarteDematMapperImpl();
  }

  @Bean
  public IDBCLCStructMapper idbclcStructMapper() {
    return new IDBCLCStructMapperImpl();
  }

  @Bean
  public MapperContractTPMaille mapperContractTPMaille() {
    return new MapperContractTPMailleImpl();
  }

  @Bean
  public AlmerysProductMapper almerysProductMapper() {
    return new AlmerysProductMapperImpl() {};
  }
}
