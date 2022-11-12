package com.mycompanylowcarpower.quoting.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class QuotingService {
  private final RestTemplate restTemplate;

  public QuotingService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  // Retourne le prix actuel en fonction du nbkwh fournit par l'utilisateur
  public BigDecimal getPriceByNbKwh(BigDecimal nbkwh) {

    BigDecimal priceKwhMixEnergie = getPriceMixEnergie(nbkwh);
    BigDecimal monthlyEstimate = nbkwh.multiply(priceKwhMixEnergie);

    return monthlyEstimate;
    //return priceKwhMixEnergie.floatValue();
  }

  // Calcul price KwhMix Energie pour obtenir le prix des deux energies
  private BigDecimal getPriceMixEnergie(BigDecimal nbkw) {

    BigDecimal priceCoalFired = getCoalFiredPrice(nbkw); // l'energie salle

    BigDecimal priceLowCarb = getPriceLowCarb(); // l'energie propre
    //BigDecimal priceKwhMixEnergie = priceCoalFired.multiply(BigDecimal.valueOf(19)).
      //  add(nbkw);
        //add(priceLowCarb.getPrice()).multiply(BigDecimal.valueOf(81));
    return priceCoalFired.add(priceLowCarb); // Calcul à revoir

  }

  // Méthode qui appel un micro-service Lowcarb
  public BigDecimal getPriceLowCarb() {
    final MomentPrice mp =
        restTemplate.getForObject("http://localhost:8090/api/lowcarb/latest",
            MomentPrice.class);
    return BigDecimal.ZERO;
  }
  private <T> RestTemplate getRestTemplate(T forObject) {
    return restTemplate;
  }

  // Calcul prix fixe de 10 dollars pour 256kwh pour l’énergie carbonée.
  private BigDecimal getCoalFiredPrice(BigDecimal nbkwh) {
    BigDecimal price =
        BigDecimal.valueOf(10);
    BigDecimal priceCoalFired =
        price.multiply(nbkwh).divide(BigDecimal.valueOf(256));
    return priceCoalFired;
  }

}
