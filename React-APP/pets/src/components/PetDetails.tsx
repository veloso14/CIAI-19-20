import React from 'react';
import {Pet} from "./PetList";
import AppointmentList, {Appointment} from "./AppointmentList";
import {GlobalState} from "../App";
import {fetchPet, updatePetRequest} from "../actions/PetActions";
import {connect} from "react-redux";
import {useParams} from "react-router-dom"
import useForm from "react-hook-form";
import Container from "react-bootstrap/Container";
import Image from "react-bootstrap/Image"
import Accordion from "react-bootstrap/Accordion";
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";
import Spinner from "react-bootstrap/Spinner";

type FormData = {
    newName: string;
    newSpecies: string;
}

/*
* #TODO: Update pet request não funciona (erro 500)
* */

const PetDetails = (props: { pet: Pet, isFetching: boolean, appointments: Appointment[], loadPet: (id: string) => void, updatePet: (id: string, pet: Pet) => void }) => {
    let {id} = useParams();

    const {register, setValue, handleSubmit, errors} = useForm<FormData>();
    const onSubmit = handleSubmit(({newName, newSpecies}) => {
        console.log(newName, newSpecies);
        props.updatePet(id as string, {
            name: newName,
            species: newSpecies,
            id: props.pet.id,
            ownerID: props.pet.ownerID
        })
        setValue("newName", "")
        setValue("newSpecies", "")
    });

    React.useEffect(() => {
        console.log("run effect details: " + id as string);
        props.loadPet(id as string);
    }, []);

    const content = (
        <Container>
            <br/>
            <h1 className="text-center">Details</h1><br/>

            {props.pet.species === "dog" &&
            <Image className="float-left mr-5" width={200} fluid src={require(`../images/dog.jpg`)}
                   roundedCircle/>}
            {props.pet.species === "cat" &&
            <Image className="float-left mr-5" width={"20%"} fluid src={require(`../images/cat.jpg`)}
                   roundedCircle/>}
            {props.pet.species === "bird" &&
            <Image className="float-left mr-5" width={"20%"} fluid src={require(`../images/bird.jpg`)}
                   roundedCircle/>}

            <div>
                <h5>Pet name: </h5>
                <p>{props.pet.name}</p>
                <h5>Pet species: </h5><p>{props.pet.species}</p>
                {/*<h5>Owner id: </h5><p>{props.pet.ownerID}</p>*/}
            </div>

            <br/>
            {props.appointments.length > 0 && <AppointmentList appointments={props.appointments}/>}

            <br/>
            <Accordion>
                <Card>
                    <Card.Header>
                        <Accordion.Toggle as={Button} variant="link" eventKey="0">
                            Edit pet
                        </Accordion.Toggle>
                    </Card.Header>
                    <Accordion.Collapse eventKey="0">
                        <Card.Body>
                            <form onSubmit={onSubmit}>
                                <div className="form-group">
                                    <label>Enter new pet name</label>
                                    <input className="form-control" id="newName" name="newName"
                                           ref={register({required: true})}/>
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
                        </Card.Body>
                    </Accordion.Collapse>
                </Card>

            </Accordion>
            <br/>
        </Container>

    );

    const loadingContent = (
        <Spinner animation="border" role="status">
            <span className="sr-only">Loading...</span>
        </Spinner>
    );

    return (
        <Container>
            {props.isFetching ? loadingContent : content}
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