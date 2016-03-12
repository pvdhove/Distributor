package vendingmachine.components;

import javax.swing.Timer;

import vendingmachine.states.ColdWater;
import vendingmachine.states.Idle;
import vendingmachine.states.NoWater;
import vendingmachine.ui.TemperatureListener;

public class HeatingSystem {

  private static final double MIN_TEMPERATURE = 90.0;
  private static final double MAX_TEMPERATURE = 96.0;
  private static final double COLD_LIMIT = 80.0;
  private static final double RUNNING_WATER_TEMPERATURE = 60.0;
  
  //private static final double diffTemperature = 2000/(4.18 * 2500); // Q/(C*m)
  
  private boolean waterSupply;

  private double temperature;

  private boolean heating;
  private TemperatureListener observer;
  private final Context context;
  
  private final Timer timer;

  public HeatingSystem(Context context) {
    this.waterSupply = true;
    this.temperature = 90.1;
    this.heating = false;
    this.context = context;

    timer = new Timer(1000, e -> this.updateTemperature());
    timer.start();
  }

  public double getTemperature() {
    return this.temperature;
  }

  public void setObserver(TemperatureListener observer) {
    this.observer = observer;
  }

  public void setTemperature(double temperature) {
    if (waterSupply) {
      this.temperature = temperature;
      if (observer != null) {
        observer.setTemperature(this.temperature);
      }
      updateState();
    }
  }

  public void setWaterSupply(boolean bool) {
    if (!waterSupply && bool) {
      temperature = RUNNING_WATER_TEMPERATURE; //Running water is reintroduced in the system
      updateState();
      timer.restart();
    } else if (waterSupply && !bool) {
      setTemperature(-1); //peu propre
      context.changeState(NoWater.getInstance());
      timer.stop();
    }
    this.waterSupply = bool;
  }

  private void updateState() {
    if (heating && temperature > MAX_TEMPERATURE) {
      heating = false;
    } else if (!heating && temperature < MIN_TEMPERATURE) {
      heating = true;
    }
    
    if (context.getState() == ColdWater.getInstance() && temperature >= COLD_LIMIT) {
      context.changeState(Idle.getInstance());
    } else if (context.getState() != ColdWater.getInstance() && temperature < COLD_LIMIT) {
      context.changeState(ColdWater.getInstance());
    }
  }

  private void updateTemperature() {
    if (waterSupply) {
      if (heating) {
        //setTemperature(temperature + diffTemperature);
        setTemperature(temperature - 100*0.6*(temperature - 150)/(4.18*2500));
        //http://www.engineeringtoolbox.com/convective-heat-transfer-d_430.html
      } else {
        setTemperature(temperature - 100*0.6*(temperature - 20)/(4.18*2500));
      }
    }
  }

  public boolean isWaterSupplyEnabled() {
    return waterSupply;
  }
  
  public void drinkOrdered() {
    if (waterSupply) {
      setTemperature((4*temperature + RUNNING_WATER_TEMPERATURE)/5);
      updateState();
    }
  }
  
}
  
  /*
   * if (heating) {
        timeWarming += 1;
        setTemperature(temperature + (-10)
            * (Math.exp(-0.0405 * (timeWarming + 1)) - Math.exp(-0.0405 * timeWarming)));
        // http://luciole.ca/gilles/mat265/chap3/var-temp.html
      } else {
        timeCooling += 1;
        setTemperature(temperature - 75 * Math.pow((61 / 75.0), (timeCooling / 400.0))
            * (1 - Math.pow(61 / 75.0, 1 / 400.0)));
      }
      */
   