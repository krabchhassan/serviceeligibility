package com.cegedim.next.serviceeligibility.core.model.domain.generic;

import java.io.Serializable;

/** Interface commune a toutes les entites de l'application. */
public interface GenericDomain<T> extends Serializable, Cloneable, Comparable<T> {}
