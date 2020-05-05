package concerttours.daos;

import concerttours.model.BandModel;

import java.util.List;

public interface BandDAO {
    /**
     * Retorna todas as bandas persistidas. Se não houver nenhuma, retorna uma lista vazia. F
     *
     */
    List<BandModel> findBands();

    /**
     * Retorna todas as bandas pelo code. Se não houver nenhuma, retorna uma lista vazia. F
     *
     * @param code
     * @return
     */
    List<BandModel> findBandsByCode(String code);
}
