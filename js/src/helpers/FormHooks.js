import { useState } from 'react';
import { partialRight, ifElse, includes } from 'ramda';


const useSignUpForm = (callback, state) => {
    const [inputs, setInputs] = useState(state);
    const setTypeInput = (event, inputs, type) => ifElse(includes("."), () => nestedInput(event, inputs, type), () => normalInput(event, inputs, type))([...event.target.name]);
    const normalInput = (event, inputs, type) => ({ ...inputs, [event.target.name]: type(event.target.value) });
    const nestedInput = (event, inputs, type) => {
        let { identifier } = { ...inputs };
        identifier[event.target.name.split(".")[1]] = type(event.target.value);
        return { identifier, ...inputs }
    }
    const stringSetInput = partialRight(setTypeInput, [String]);
    const numberSetInput = partialRight(setTypeInput, [Number]);
    const handleSubmit = event => {
        if (event) {
            event.preventDefault();
        }
        callback();
    }
    const handleInputChange = (event) => {
        event.persist();
        setInputs(inputs => stringSetInput(event, inputs))
    }
    const handleNumericInputChange = event => {
        event.persist();
        setInputs(inputs => numberSetInput(event, inputs))
    }
    return {
        handleSubmit,
        handleInputChange,
        handleNumericInputChange,
        inputs
    };
}

export default useSignUpForm;
