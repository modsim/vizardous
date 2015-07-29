package vizardous.computations;

import java.util.Map;

/**
 * Interface that hides the implementation details of a CellularState.
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public interface CellularStateInterface {
	
		public void setSystemState(MolecularSystemInterface system, SystemStateInterface state);
		
		public Map<MolecularSystemInterface, SystemStateInterface> getStates();
		
		@Override
		public String toString();
		
		@Override
		public boolean equals(Object obj);
		
		@Override
		public int hashCode();

}
