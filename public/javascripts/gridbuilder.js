var gridBuilder = {

	gridContainer : null,
	infosContainers : {
        elapsedTime : null,
        solvedCount : null,
        solvedPercentage : null 
	},
	buttons : {
        generate : null,
        start : null,
        stop : null
	},
    grid : null,
    
    create : function(grid){
        var self = this;
        self.grid = grid;
        this._init();
        this._setCells();

    },
    _init : function(){
        
        var self = this;
                   
        self.gridContainer.empty();  
    
        var table = $('<table></table>').attr("id","grid");

        for(var y = 1 ; y <= self.grid.height ; y++){
            var row = $('<tr></tr>');
            
            for(var x = 1 ; x <= self.grid.width ; x++){
                var col = $('<td></td>').attr("data-x",x).attr("data-y",y);
                row.append(col);
            }
            table.append(row);
        }
        
        self.gridContainer.append(table);  
        
    },
    _setCells : function(){
        
        var self = this;
        
        $(self.grid.blackCells).each(function(i,blackCell){
            self._setBlackCell(blackCell);
        });
        $(self.grid.whiteCells).each(function(i,whiteCell){
            self._setWhiteCell(whiteCell);
        });
        
    },
    _getCell : function(x,y){
        var self = this;
        return $('table#grid td[data-x='+x+'][data-y='+y+']',self.gridContainer);
    },
    _setBlackCell : function(blackCell){
        var self = this;
         self._getCell(blackCell.x,blackCell.y).addClass("black-cell");
    },
    _setWhiteCell : function(whiteCell){
        var self = this;
         self._getCell(whiteCell.x,whiteCell.y).text(whiteCell.letter).addClass("white-cell");
    },
    _updateWhiteCell : function(whiteCell){
        var self = this;
         self._getCell(whiteCell.x,whiteCell.y).text(whiteCell.letter);
    },
    _updateGrid : function(grid){
    	var self = this;
    	self.grid = grid;
        $(self.grid.whiteCells).each(function(i,whiteCell){
            self._updateWhiteCell(whiteCell);
        });
    },
    _updateInfos: function(infos){
    	var self = this;
    	self.infosContainers.elapsedTime.text(infos.elapsedTime/1000+"s");
    	self.infosContainers.solvedCount.text(infos.solvedCount+"/"+infos.totalCount);
    	self.infosContainers.solvedPercentage.text(parseInt(infos.solvedCount/infos.totalCount*100)+"%"); 
    },
    updateButtonsStates : function(processing){
    	var self = this;
    	if(processing){
            self.buttons.start.button("disable");
            self.buttons.stop.button("enable");  
            self.buttons.generate.button("disable");  
            $('#grid-width-slider').slider("disable");
            $('#grid-height-slider').slider("disable");
    	}else{
    		self.buttons.start.button("enable");
    		self.buttons.stop.button("disable");
    		self.buttons.generate.button("enable");
    		$('#grid-width-slider').slider("enable");
            $('#grid-height-slider').slider("enable");
    	}
    },
    listen : function(url,solverId,delay){
    	var self = this;

            $.ajax({
                url : url,
                data : {
                    solverId : solverId
                },
                dataType : "text",
	                success:function(solver){
	                	var solver = $.parseJSON(solver);
	                	if(solver != null){
	                	    
	                		self._updateGrid(solver.grid);
	                		self._updateInfos(solver.infos);
		                	
	                		if(!solver.solved){
		                		 setTimeout(function(){self.listen(url,solverId,delay)},delay);
		                	}else{
		                		self.updateButtonsStates(false);
		                	}
		                }
	                	
	                }
	            });
  
        }
    
    }