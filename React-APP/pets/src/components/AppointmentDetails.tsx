import React from 'react';
import Container from "react-bootstrap/Container";
import {useParams} from "react-router";
import {GlobalState} from "../App";
import {Appointment} from "./AppointmentList";
import {connect} from "react-redux";
import {getData} from "../Utils/NetworkUtils";

const AppointmentDetails = (props: { appointments: Appointment[] }) => {
    let {id} = useParams();
    const [apt, setApt] = React.useState({id: -1, desc: "", date:Date.now()})

    const loadAppointment = (id:string) => {
        return getData(`/appointments/${id}`, {} as Appointment)
            .then(data => {
                console.log("fetch appointment: " + JSON.stringify(data))
                setApt(data)
            })
    };


    React.useEffect(() => {
      loadAppointment(id as string)
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