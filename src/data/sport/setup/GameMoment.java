package data.sport.setup;

import process.visitor.gamemoment.GameMomentVisitor;

public abstract class GameMoment {

   public GameMoment() {

   }

   public abstract <T> T accept(GameMomentVisitor<T> visitor);

}
