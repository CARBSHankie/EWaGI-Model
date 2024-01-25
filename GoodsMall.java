package eWAGI_Firms;

import java.util.ArrayList;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class GoodsMall extends Mall{
	
	
	ArrayList<GoodsInventory> firmInventories;
	
	
	GoodsMall(ContinuousSpace<Object> space, Grid<Object> grid,int mallID,int sectorID){
		super(space,  grid, mallID, sectorID);
		
		firmInventories = new ArrayList<GoodsInventory>() ;
		
		}
	
	
	

	void setup() {
		

		totalDemand = 0;
		demandFirms= 0;
		totalSales = 0;
		avPrice= 0;
		totalSupply= 0;
		
		
		for(int i=0; i < firmInventories.size();i++) {
			
			firmInventories.get(i).totalDemand = 0;
			
			
			firmInventories.get(i).soldQuantities = 0;
			firmInventories.get(i).revenues = 0;
			
			firmInventories.get(i).salesToFirms = 0;
			firmInventories.get(i).salesToHouseholds = 0;
			
			totalSupply += firmInventories.get(i).inventoryStock;
			
			avPrice += firmInventories.get(i).inventoryStock* firmInventories.get(i).price;
		
		
		}
		
		
		avPrice = avPrice / totalSupply;
		
		
	}
	
	
	
	void allocateOrders() {
		
		
		primaryDemand = totalDemand;
		
		double order = 0;
		int index_i = -1, index_j= -1;

		
		for(int i=0; i < firmInventories.size();i++) {
			
			
			firmInventories.get(i).primaryDemand = firmInventories.get(i).sumOrders;
		
			firmInventories.get(i).sumOrders = 0;
		
			for(int j=0; j < firmInventories.get(i).orderBook.size();j++) {
				firmInventories.get(i).sumOrders += firmInventories.get(i).orderBook.get(j).orderQuantity;
				order =  firmInventories.get(i).orderBook.get(j).orderQuantity;
				
				if(order!=order ) {
					index_i = i;
					index_j = j;
					
				}
				
				totalDemand += firmInventories.get(i).orderBook.get(j).orderQuantity;
				if(firmInventories.get(i).orderBook.get(j).isFirm) {
					
					demandFirms+= firmInventories.get(i).orderBook.get(j).orderQuantity;
					
				}
				
				
				
					
			}
			
			firmInventories.get(i).totalDemand += firmInventories.get(i).sumOrders;
			
			if(firmInventories.get(i).sumOrders <= firmInventories.get(i).inventoryStock) {
				
				
				firmInventories.get(i).stockOut = false;
	
				
				for(int j=0; j < firmInventories.get(i).orderBook.size();j++) {

					
						
						firmInventories.get(i).orderBook.get(j).acceptedQuantity = firmInventories.get(i).orderBook.get(j).orderQuantity;
						
						double acceptedQuantity = firmInventories.get(i).orderBook.get(j).acceptedQuantity;
						
						firmInventories.get(i).orderBook.get(j).quality = firmInventories.get(i).quality;
						
						firmInventories.get(i).inventoryStock -= firmInventories.get(i).orderBook.get(j).acceptedQuantity ;
						
					
						firmInventories.get(i).soldQuantities += firmInventories.get(i).orderBook.get(j).acceptedQuantity;
						totalSales += firmInventories.get(i).orderBook.get(j).acceptedQuantity;
						
						firmInventories.get(i).orderBook.get(j).invoiceAmount = firmInventories.get(i).orderBook.get(j).acceptedQuantity *firmInventories.get(i).price; 
						
						firmInventories.get(i).revenues +=firmInventories.get(i).orderBook.get(j).invoiceAmount;
						
						

						if(firmInventories.get(i).orderBook.get(j).isFirm) {
							
							firmInventories.get(i).salesToFirms += firmInventories.get(i).orderBook.get(j).acceptedQuantity;
							
						}else {
							
							firmInventories.get(i).salesToHouseholds += firmInventories.get(i).orderBook.get(j).acceptedQuantity;
							
						}
						
						
						
						

				}
				
				
				
				
				
			}else {
				
				firmInventories.get(i).stockOut = true;
				

				double rationingQuota = firmInventories.get(i).inventoryStock/firmInventories.get(i).sumOrders;
				
				
				
				for(int j=0; j < firmInventories.get(i).orderBook.size();j++) {
					
					
		
					firmInventories.get(i).orderBook.get(j).acceptedQuantity = firmInventories.get(i).orderBook.get(j).orderQuantity* rationingQuota;

					firmInventories.get(i).orderBook.get(j).quality = firmInventories.get(i).quality;
					
					double acceptedQuantity = firmInventories.get(i).orderBook.get(j).acceptedQuantity;
					
					
					firmInventories.get(i).inventoryStock -= firmInventories.get(i).orderBook.get(j).acceptedQuantity ;
					
					
					firmInventories.get(i).soldQuantities += firmInventories.get(i).orderBook.get(j).acceptedQuantity;
					
					totalSales += firmInventories.get(i).orderBook.get(j).acceptedQuantity;
					
					firmInventories.get(i).orderBook.get(j).invoiceAmount = firmInventories.get(i).orderBook.get(j).acceptedQuantity *firmInventories.get(i).price; 
					
					firmInventories.get(i).revenues +=firmInventories.get(i).orderBook.get(j).invoiceAmount;
					
					

					if(firmInventories.get(i).orderBook.get(j).isFirm) {
						
						firmInventories.get(i).salesToFirms += firmInventories.get(i).orderBook.get(j).acceptedQuantity;
						
					}else {
						
						firmInventories.get(i).salesToHouseholds += firmInventories.get(i).orderBook.get(j).acceptedQuantity;
						
					}
					

				}
				
				
				
				
				
				
			}
			
			
			
			
			
		}
		
		
		
	}
	
	
	void closeMall() {
		
		excessDemand = primaryDemand - totalSales;
		
		double inventoriesLeftOver = 0;
		
		for(int i=0; i < firmInventories.size();i++) {
	
		
		firmInventories.get(i).totalExcessDemand = excessDemand;
		
		firmInventories.get(i).totalMarketSales = totalSales;
		
		firmInventories.get(i).avPrice= avPrice;
		
		if(totalSales>0) {
			firmInventories.get(i).marketShare = firmInventories.get(i).soldQuantities/totalSales;
		
		}else {
			firmInventories.get(i).marketShare = 0.0;
		}
		
		}
		
		System.out.println("Sector "+this.sectorID+"  Total supply:  "+this.totalSupply+" 	Total demand:"+this.totalDemand+ "Demand Firms 	"+demandFirms);
		
		
	}


}
