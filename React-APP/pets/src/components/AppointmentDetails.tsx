import React from 'react';
import Container from "react-bootstrap/Container";
import {useParams} from "react-router";
import {GlobalState} from "../App";
import {fetchPet, updatePetRequest} from "../actions/PetActions";
import {Appointment, Pet} from "./PetList";
import {connect} from "react-redux";

const AppointmentDetails = (props: { appointments: Appointment[] }) => {
    let {id} = useParams();
    const [apt, setApt] = React.useState({id: -1, desc: "", date:Date.now()})


    React.useEffect(() => {
        let appointments = props.appointments
        appointments.map((apt) => {
            if ((apt.id) == +(id as string)) {
                setApt(apt)
            }
        })
    }, [])


    return (
        <Container>
            <h1 className="text-center">AppointmentDetails</h1>
            <h3>Id: {apt.id}</h3>
            <h3>Desc: {apt.desc}</h3>
            <h3>Date: {apt.date}</h3>
        </Container>
    );
}


const mapStateToProps = (state: GlobalState) => {
    return {
        pet: state.pets.pet,
        appointments: state.pets.appointments,
        isFetching: state.pets.isFetching
    }
};
const mapDispatchToProps = (dispatch: any) => {
    return {}
};
export default connect(mapStateToProps, mapDispatchToProps)(AppointmentDetails);