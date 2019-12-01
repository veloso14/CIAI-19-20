import React from 'react';
import Container from "react-bootstrap/Container";
import useForm from "react-hook-form";
import {getData} from "../Utils/NetworkUtils";
import {receivePet} from "../actions/PetActions";

interface Vet {
    id:string,
    name:string
}

type FormData = {
    petName: string;
    petSpecies: string;
}

const AddAppointmentForm = () => {
    const {register, setValue, handleSubmit, errors} = useForm<FormData>();

    const [vets, setVets] = React.useState([]);

    const onSubmit = handleSubmit(({petName, petSpecies}) => {
        console.log(petName, petSpecies);
    });

    React.useEffect(() => {
        getData("/vets/", [])
            .then(data => {
                console.log("appointment form effect: " + JSON.stringify(data));
                setVets(data);
            })
    }, []);

    let vetOptionsList = vets.length > 0
        && vets.map((item, i) => {
            return (
                // #TODO mostrar options sem ts ignore default value tem de ser array de vet senao da undefined 
                // @ts-ignore
                <option key={i} value={item.id}>{item.name}</option>
            )
        });

    return (
        <Container>
            <br/>
            <h1 className="text-center">Create a new Appointment</h1>
            <form onSubmit={onSubmit}>
                <div className="form-group">
                    <label>Pet Name</label>
                    <input className="form-control" id="petName" name="petName" ref={register({required: true})}/>
                    {errors.petName && 'Pet name is required'}
                </div>
                <div className="form-group">
                    <label>Vet</label>
                    <select className="form-control" id="vet" name="vet" ref={register}>
                        {vetOptionsList}
                    </select>
                </div>
                <input className="btn btn-primary float-right" type="submit" value="Add Pet"/>
            </form>
        </Container>
    );
}

export default AddAppointmentForm;