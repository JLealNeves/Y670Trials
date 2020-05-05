package concerttours.service;

import concerttours.model.BandModel;

import java.util.List;

public interface BandService {
    /**
     * Retorn todas as bandas. Se não houver nenhuma, retorna uma lista vazia.
     * @return
     */
    List<BandModel> getBands();

    /**
     * Retorna a banda de acordo com seu code. Se não houver nenhuma, retorna uma lista vazia.
     * @param code
     * @return
     */
    BandModel getBandForCode(String code);
}
