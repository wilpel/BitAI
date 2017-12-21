function NeuralNetwork(layer_objs) {
    this.layer_objs = layer_objs;

    //Functions
    this.compute = function(input) {
        this.layer_objs[0].feedForward(input);
        
        for (var i = 1; i < this.layer_objs.length; i++) {
            this.layer_objs[i].feedForward(this.layer_objs[i - 1].output);
        }     
        //console.log("Output: ", this.layer_objs[this.layer_objs.length-1].output);
        return this.layer_objs[this.layer_objs.length -1].output;
    }
    this.backProp = function(expected){
        this.expected = expected;
    
        for (var i = this.layer_objs.length -1; i >= 0; i--) {       
            if (i == this.layer_objs.length -1) {
                //console.log("Expected__________________",this.expected)
                this.layer_objs[i].backPropOut(this.expected);
            } else {
                this.layer_objs[i].backPropHidden(this.layer_objs[i +1].gamma, this.layer_objs[i + 1].weights);
            }       
        }
            
        for (var i = 0; i < this.layer_objs.length; i++) {
            this.layer_objs[i].updateWeights();          
        }
    }
    
}
