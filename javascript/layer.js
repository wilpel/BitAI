function layer(i_c, o_c, type){

    this.type = 'rnn, fc, input, output'
    this.type = type

    this.output_count= o_c;
    this.input_count = i_c;

    //Feed forward vars
    this.output = new Array(this.output_count)
    this.weights = new Array(this.output_count);
    this.input = new Array(this.input_count)
    //-----

    //Backprop vars
    this.gamma = new Array(this.output_Count)
    this.error = new Array(this.output_Count)
    this.weightsDelta = new Array(this.output_count);
    //-----

    //Generate weights
    for (var i = 0; i < this.output_count; i++){
        this.weights[i] = new Array(this.input_count);
        this.weightsDelta[i] = new Array(this.input_count);

        for(var j= 0; j< this.input_count; j++){
            this.weights[i][j] = Math.random() -0.5;
        }
    }

    //Square function
    this.square = function(val) {
        return 1 - (val *val)
    }

    //Feed data forward
    this.feedForward = function(input) {
        this.input = input;
        for (var i = 0; i < this.output_count; i++) {
            this.output[i] = 0;
            for (var j = 0; j < this.input_count; j++) {
                this.output[i] += (this.input[j] * this.weights[i][j]);
            }
            this.output[i] = Math.tanh(this.output[i]);
        }  
    }
    
    //Backprop output
    this.backPropOut = function(expected) {
        this.expected = expected;
        for(var i = 0; i < this.output_count; i++) {
            this.error[i] = (this.output[i] - this.expected[i]);
        }
        for(var j = 0; j < this.output_count; j++) {
            this.gamma[j] = this.error[j] * this.square(this.output[j]) //Square
        }

        for (var k = 0; k < this.output_count; k++) {
            for (var l = 0; l < this.input_count; l++) {
                this.weightsDelta[k][l] = this.gamma[k] * this.input[l];
            }
        }
    }

    //Backprop hidden layers
    this.backPropHidden = function(gammaF, weightsF) {
        this.gammaF = gammaF;
        this.weightsF = weightsF;
        for (var i = 0; i < this.output_count; i++) {
            this.gamma[i] = 0;    
            for (var j = 0; j < this.gammaF.length; j++) {
                this.gamma[i] += this.gammaF[j] * this.weightsF[j][i];
            }
            this.gamma[i] *= this.square(this.output[i]); //Square
        }
    
        for (var i = 0; i < this.output_count; i++) {
            for (var j = 0; j < this.input_count; j++) {
                this.weightsDelta[i][j] = this.gamma[i] * this.input[j];
    
            }
        }
    }

    //Update all weights
    this.updateWeights = function() {
		for (var i = 0; i < this.output_count; i++) {
			for (var j = 0; j < this.input_count; j++) {
				this.weights[i][j] -= this.weightsDelta[i][j] * 0.0000333; //Lrning rate ******REQUIRED*****
            }
		}
    }
}

layer.buildLayers = function(neuronCountArr){
    this.layers = []
    this.neuronCountArr = neuronCountArr;
    for (var i = 0; i < this.neuronCountArr.length -1; i++) {
        this.layers.push(new layer(this.neuronCountArr[i], this.neuronCountArr[i + 1]));
    }
    this.layers.push(new layer(this.neuronCountArr[i], this.neuronCountArr[i]));
    return this.layers
}