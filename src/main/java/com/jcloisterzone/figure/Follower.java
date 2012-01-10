package com.jcloisterzone.figure;

import com.jcloisterzone.Expansion;
import com.jcloisterzone.Player;
import com.jcloisterzone.feature.Castle;
import com.jcloisterzone.feature.City;
import com.jcloisterzone.feature.Farm;
import com.jcloisterzone.feature.Feature;
import com.jcloisterzone.feature.visitor.RemoveLonelyBuilderAndPig;
import com.jcloisterzone.game.Game;

public abstract class Follower extends Meeple {

	private static final long serialVersionUID = -659337195197201811L;

	public Follower(Game game, Player player) {
		super(game, player);
	}

	public int getPower() {
		return 1;
	}
	
	@Override
	public boolean canBeEatenByDragon() {
		return !(getFeature() instanceof Castle);
	}

	@Override
	protected void checkDeployment(Feature f) {
		if (f.isFeatureOccupied()) {
			throw new IllegalArgumentException("Feature is occupied.");
		}
		super.checkDeployment(f);
	}


	//TODO ??? can be this in score visitor instead of here ???
	public void undeploy(boolean checkForLonelyBuilderOrPig) {
		//store ref which is lost be super call 
		Feature piece = getFeature();		
		super.undeploy(checkForLonelyBuilderOrPig); //clear piece
		if (checkForLonelyBuilderOrPig &&
				game.hasExpansion(Expansion.TRADERS_AND_BUILDERS) &&
				(piece instanceof City || piece instanceof Farm)) {
			RemoveLonelyBuilderAndPig visitor = new RemoveLonelyBuilderAndPig(getPlayer());
			piece.walk(visitor);
			if (visitor.getMeepleToRemove() != null) {
				visitor.getMeepleToRemove().undeploy(false);
			}
		}
	}


}
