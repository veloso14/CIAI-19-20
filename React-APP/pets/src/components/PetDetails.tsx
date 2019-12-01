import React from 'react';
import {Appointment, Pet} from "./PetList";
import {GlobalState} from "../App";
import {updatePetRequest, fetchPet} from "../actions/PetActions";
import {connect} from "react-redux";
import {Link, useParams} from "react-router-dom"
import useForm from "react-hook-form";
import Container from "react-bootstrap/Container";
import Image from "react-bootstrap/Image"
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import AppointmentList from "./AppointmentList";

type FormData = {
    newName: string;
    newSpecies: string;
}

/*
* #TODO: Update pet request não funciona (erro 500)
* */

const PetDetails = (props: { pet: Pet, appointments: Appointment[], loadPet: (id: string) => void, updatePet: (id: string, pet: Pet) => void }) => {
    let {id} = useParams();

    const {register, setValue, handleSubmit, errors} = useForm<FormData>();
    const onSubmit = handleSubmit(({newName, newSpecies}) => {
        console.log(newName, newSpecies);
        props.updatePet(id as string, {name: newName, species: newSpecies, id: props.pet.id})
    });

    React.useEffect(() => {
        console.log("run effect details");
        props.loadPet(id as string);
    }, []);
    return (
        <Container>
            <h1 className="text-center">Details</h1>
            <Row>
                <Image width={200} height={200} fluid src={require('../images/dog.jpg')} roundedCircle/>
                <Col>
                    <h5>Pet name: {props.pet.name}</h5><br/>
                    <h5>Pet species: {props.pet.species}</h5><br/>
                </Col>
            </Row>
            <br/>
            {props.appointments.length > 0 && <AppointmentList/>}
            <br/>
            <h1 className="text-center">Edit Pet</h1>
            <form onSubmit={onSubmit}>
                <div className="form-group">
                    <label>Enter new pet name</label>
                    <input className="form-control" id="newName" name="newName" ref={register({required: true})}/>
                    {errors.newName && 'New pet name is required'}
                </div>
                <div className="form-group">
                    <label>Enter new species</label>
                    <select className="form-control" id="newSpecies" name="newSpecies" ref={register}>
                        <option value="cat">Cat</option>
                        <option value="dog">Dog</option>
                        <option value="bird">Bird</option>
                    </select>
                </div>
                <input className="btn btn-primary float-right" type="submit" value="Edit Pet"/>
            </form>

        </Container>
    );
};


const mapStateToProps = (state: GlobalState) => {
    return {
        pet: state.pets.pet,
        appointments: state.pets.appointments,
        isFetching: state.pets.isFetching
    }
};
const mapDispatchToProps = (dispatch: any) => {
    return {
        loadPet: (id: string) => {
            dispatch(fetchPet(id))
        },
        updatePet: (id: string, newPet: Pet) => {
            dispatch(updatePetRequest(id, newPet))
        },
    }
};
export default connect(mapStateToProps, mapDispatchToProps)(PetDetails);